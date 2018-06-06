#include "PngUtils.h"

#include <stdlib.h>
#include <sys/stat.h>

#define dUsePallete	1	//パレット対応

// ------------------------------
// Accesser
// ------------------------------
/**
 * AssetsからPNGを読み込む
 * 注). freeで解放すること
 */
void PngUtils::createPNGData(AAsset *pAsset, GameBitmapInfo *pInfo, unsigned char **ppData)
{
	GameBitmapInfo	*imageInfo		=	pInfo;
	unsigned char	*pDataBuffer	=	NULL;

	int imageSize	=	0;
	int channel		=	0;
	int depth		=	0;

	png_uint_32	width			=	0;
	png_uint_32	height			=	0;
	int			bit_depth		=	0;
	int			color_type		=	0;
	int			interlace_type	=	0;

	png_structp	pPng		=	NULL;
	png_infop	pPngInfo	=	NULL;

#if dUsePallete
	//パレット情報用変数
	int			numOfColorInPalette	=	0;		//色数
	png_colorp	pColorPalette		=	NULL;		//PNGパレット

	//tRNS情報用変数(indexカラーの場合は、tRNSは透明度を示す)
	int			numOfTRNSValue	=	0;
	png_bytep	pTRNSValues		=	NULL;
#endif

	pPng	=	png_create_read_struct (PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	if (!pPng)
	{
		LOGE("createPNGData png_create_read_struct: failed");
		goto endproc;
	}

	pPngInfo	=	png_create_info_struct (pPng);
	if (!pPngInfo)
	{
		LOGE ("createPNGData png_create_info_struct: failed");
		goto endproc;
	}

	if (setjmp (png_jmpbuf (pPng)))
	{
		LOGE ("createPNGData setjmp: failed");
		goto endproc;
	}

	// libpngの読み込み処理をAndroidのAssetから読み込めるようにする
	png_set_read_fn (pPng, (void *)pAsset, pngReadFileCallback);

	// PNG画像の情報を取得する
	png_read_info (pPng, pPngInfo);
	png_get_IHDR (pPng, pPngInfo, &width, &height, &bit_depth, &color_type, &interlace_type, NULL, NULL);

	if (interlace_type != PNG_INTERLACE_NONE)
	{
		LOGE("createPNGData interlace_type: not supported");
		goto endproc;
	}

#if dUsePallete
	//パレットがあれば、パレットを読み込む（PNGパレットは、libpng側で開放する）
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//PNGパレットを読み込む
		png_get_PLTE (pPng, pPngInfo, &pColorPalette, &numOfColorInPalette);
		png_get_tRNS (pPng, pPngInfo, &pTRNSValues, &numOfTRNSValue, NULL);
	}
#endif

	// チャンネル数の取得
	switch (color_type)
	{
		case PNG_COLOR_TYPE_RGB:
			channel = 3;
			break;

		case PNG_COLOR_TYPE_GRAY:
			channel = 1;
			break;

		case PNG_COLOR_TYPE_RGB_ALPHA:
			channel = 4;
			break;

		case PNG_COLOR_TYPE_GRAY_ALPHA:
			channel = 1;
			break;

#if dUsePallete	//インデックスカラーの時は、RGBAのデータで返却する
		case PNG_COLOR_TYPE_PALETTE:
			channel = 4;
			break;
#endif
		default:
			channel = 4; // ARGBに変更
			break;
	}


	// 出力画像を格納する連続領域を確保する
	imageSize	=	width * height * channel;

	pDataBuffer	=	(unsigned char *)calloc (imageSize, sizeof(unsigned char));
	if (!pDataBuffer)
	{
		// メモリ不足
		LOGE("createPNGData malloc pDataBuffer: failed");
		goto endproc;
	}

	// 各画像情報を格納
	imageInfo->width	=	width;
	imageInfo->height	=	height;
	imageInfo->bitdepth	=	bit_depth;
#if dUsePallete
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//念のため8を入れておく
		imageInfo->bitdepth	=	8;
	}
#endif
	imageInfo->channel	=	channel;
	imageInfo->size		=	imageSize;

#if dUsePallete
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//インデックスカラーの時の処理

		png_size_t	rowBytes	=	png_get_rowbytes (pPng, pPngInfo);
		png_uint_32 rowIndex	=	0;
		int			rowOffset	=	0;

		unsigned char	*pLineBuffer	=	(unsigned char *)calloc (rowBytes, sizeof(unsigned char));
		if (!pLineBuffer)
		{
			LOGE("createPNGDataFromPath malloc pLineBuffer: failed");
			goto endproc;
		}

		//一行ごと、各ピクセルのパレットに対するインデックス値を取得し、RGBAに展開していく。
		while (rowIndex < height)
		{
			png_read_row (pPng, pLineBuffer, NULL);

			for (int idxInLine = 0; idxInLine < width; idxInLine++)
			{
				unsigned char	palletIndex		=	0;

				switch (bit_depth)
				{
					case 1:	//1bitでインデックスを表す?
					{
						const static char sBitDepth1Mask[8]	=	{
								static_cast<char> (0x80),
								static_cast<char> (0x40),
								static_cast<char> (0x20),
								static_cast<char> (0x10),
								static_cast<char> (0x8),
								static_cast<char> (0x4),
								static_cast<char> (0x2),
								static_cast<char> (0x1)
						};

						int				bufIndex	=	(idxInLine / 8);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 8);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	7 - maskIndex;				//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth1Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;

					case 2:	//2bitでインデックスを表す?
					{
						const static char sBitDepth2Mask[4]	=	{
								static_cast<char> (0xC0),
								static_cast<char> (0x30),
								static_cast<char> (0xC),
								static_cast<char> (0x3),
						};

						int				bufIndex	=	(idxInLine / 4);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 4);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	6 - (maskIndex * 2);		//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth2Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;
					case 4:	//4bitでインデックスを表す?
					{
						const static char sBitDepth4Mask[2]	=	{
								static_cast<char> (0xF0),
								static_cast<char> (0xF),
						};

						int				bufIndex	=	(idxInLine / 2);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 2);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	4 - (maskIndex * 4);		//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth4Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;

					case 8:
						palletIndex	=	*(pLineBuffer + idxInLine);
						break;
				}

				png_color	*colorData		=	pColorPalette + palletIndex;
				int			posInImageBuf	=	rowOffset + (idxInLine * 4);

				*(pDataBuffer + posInImageBuf + 0)	=	colorData->red;
				*(pDataBuffer + posInImageBuf + 1)	=	colorData->green;
				*(pDataBuffer + posInImageBuf + 2)	=	colorData->blue;
				*(pDataBuffer + posInImageBuf + 3)	=	(palletIndex < numOfTRNSValue ? *(pTRNSValues + palletIndex) : 0xFF);
			}

			rowOffset += imageInfo->width * 4;
			rowIndex++;
		}
		free (pLineBuffer);
	}
	else
#endif
		// 1行づつ読み込む
	{
		png_uint_32	i		=	0;
		int			offset	=	0;
		while (i < height)
		{
			png_read_row (pPng, pDataBuffer + offset, NULL);

			offset += imageInfo->width * 4;
			i++;
		}
	}

	// 読み込み終了
	png_read_end(pPng, NULL);

	// データを返却
	*ppData = pDataBuffer;

	endproc:
	// clean up.
	png_destroy_read_struct (&pPng, &pPngInfo, NULL);
}

/**
 * Pathを指定して、Pngファイルを取得する
 */
int
PngUtils::createPNGDataFromPath(char *filePath, GameBitmapInfo *pInfo, unsigned char **ppData)
{
	GameBitmapInfo	*imageInfo		=	pInfo;
	unsigned char	*pDataBuffer	=	NULL;

	int	imageSize	=	0;

	png_uint_32 width			=	0;
	png_uint_32 height			=	0;
	int			bit_depth		=	0;
	int			color_type		=	0;
	int			interlace_type	=	0;

	png_structp	pPng		=	NULL;
	png_infop	pPngInfo	=	NULL;

#if dUsePallete
	//パレット情報用変数
	int			numOfColorInPalette	=	0;		//色数
	png_colorp	pColorPalette		=	NULL;		//PNGパレット

	//tRNS情報用変数(indexカラーの場合は、tRNSは透明度を示す)
	int			numOfTRNSValue	=	0;
	png_bytep	pTRNSValues		=	NULL;
#endif

	//画像ファイルの読み込み
	FILE *fp	=	fopen (filePath,"r");
	if (!fp)
	{
		LOGI("createPNGFromDatPath Can't open file(%s)", filePath);
		goto endproc;
	}

	pPng	=	png_create_read_struct (PNG_LIBPNG_VER_STRING,NULL,NULL,NULL);
	if (!pPng)
	{
		LOGE("createPNGDataFromPath png_create_read_struct: failed");
		goto endproc;
	}

	pPngInfo	=	png_create_info_struct (pPng);
	if (!pPngInfo)
	{
		LOGE("createPNGDataFromPath png_create_info_struct: failed");
		goto endproc;
	}

	//ファイルポインタの設定
	png_init_io (pPng,fp);

	//画像情報取得
	png_read_info (pPng, pPngInfo);
	png_get_IHDR (pPng, pPngInfo, &width, &height, &bit_depth, &color_type, &interlace_type, NULL, NULL);

#if dUsePallete
	//パレットがあれば、パレットを読み込む（PNGパレットは、libpng側で開放する）
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//PNGパレットを読み込む
		png_get_PLTE (pPng, pPngInfo, &pColorPalette, &numOfColorInPalette);
		png_get_tRNS (pPng, pPngInfo, &pTRNSValues, &numOfTRNSValue, NULL);
	}
#endif

	imageSize	=	width * height * 4;

	//画像データ用バッファーの確保
	pDataBuffer	=	(unsigned char *)calloc (imageSize, sizeof(unsigned char));
	if (!pDataBuffer)
	{
		// メモリ不足
		LOGE("createPNGDataFromPath malloc pDataBuffer: failed");
		goto endproc;
	}

	// 各画像情報を格納
	imageInfo->width	=	width;
	imageInfo->height	=	height;
	imageInfo->bitdepth	=	bit_depth;
#if dUsePallete
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//念のため8を入れておく
		imageInfo->bitdepth	=	8;
	}
#endif
	imageInfo->channel	=	4;
	imageInfo->size		=	imageSize;

#if dUsePallete
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//インデックスカラーの時の処理

		png_size_t	rowBytes	=	png_get_rowbytes (pPng, pPngInfo);
		png_uint_32 rowIndex	=	0;
		int			rowOffset	=	0;

		unsigned char	*pLineBuffer	=	(unsigned char *)calloc (rowBytes, sizeof(unsigned char));
		if (!pLineBuffer)
		{
			LOGE("createPNGDataFromPath malloc pLineBuffer: failed");
			goto endproc;
		}

		//一行ごと、各ピクセルのパレットに対するインデックス値を取得し、RGBAに展開していく。
		while (rowIndex < height)
		{
			png_read_row (pPng, pLineBuffer, NULL);

			for (int idxInLine = 0; idxInLine < width; idxInLine++)
			{
				unsigned char	palletIndex		=	0;

				switch (bit_depth)
				{
					case 1:	//1bitでインデックスを表す?
					{
						const static char sBitDepth1Mask[8]	=	{
								static_cast<char> (0x80),
								static_cast<char> (0x40),
								static_cast<char> (0x20),
								static_cast<char> (0x10),
								static_cast<char> (0x8),
								static_cast<char> (0x4),
								static_cast<char> (0x2),
								static_cast<char> (0x1)
						};

						int				bufIndex	=	(idxInLine / 8);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 8);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	7 - maskIndex;				//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth1Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;

					case 2:	//2bitでインデックスを表す?
					{
						const static char sBitDepth2Mask[4]	=	{
								static_cast<char> (0xC0),
								static_cast<char> (0x30),
								static_cast<char> (0xC),
								static_cast<char> (0x3),
						};

						int				bufIndex	=	(idxInLine / 4);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 4);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	6 - (maskIndex * 2);		//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth2Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;
					case 4:	//4bitでインデックスを表す?
					{
						const static char sBitDepth4Mask[2]	=	{
								static_cast<char> (0xF0),
								static_cast<char> (0xF),
						};

						int				bufIndex	=	(idxInLine / 2);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 2);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	4 - (maskIndex * 4);		//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth4Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;

					case 8:
						palletIndex	=	*(pLineBuffer + idxInLine);
						break;
				}

				png_color	*colorData		=	pColorPalette + palletIndex;
				int			posInImageBuf	=	rowOffset + (idxInLine * 4);

				*(pDataBuffer + posInImageBuf + 0)	=	colorData->red;
				*(pDataBuffer + posInImageBuf + 1)	=	colorData->green;
				*(pDataBuffer + posInImageBuf + 2)	=	colorData->blue;
				*(pDataBuffer + posInImageBuf + 3)	=	(palletIndex < numOfTRNSValue ? *(pTRNSValues + palletIndex) : 0xFF);
			}

			rowOffset += imageInfo->width * 4;
			rowIndex++;
		}
		free (pLineBuffer);
	}
	else
#endif
		// 1行づつ読み込む
	{
		png_uint_32 i = 0;
		int offset = 0;
		while (i < height)
		{
			png_read_row(pPng, pDataBuffer + offset, NULL);

			offset += imageInfo->width * 4;
			i++;
		}
	}

	// 読み込み終了
	png_read_end(pPng, NULL);

	// データを返却
	*ppData = pDataBuffer;


	endproc:
	// clean up.
	png_destroy_read_struct(&pPng, &pPngInfo, NULL);

	fclose(fp);

	return 1;
}

/**
 * バイナリデータのPathからPNGを取得する
 */
void
PngUtils::createPNGFromDatPath(char *filePath, GameBitmapInfo *pInfo, unsigned char **ppData)
{
	GameBitmapInfo	*imageInfo		=	pInfo;
	unsigned char	*pDataBuffer	=	NULL;

	int	imageSize	=	0;

	png_uint_32	width			=	0;
	png_uint_32 height			=	0;
	int			bit_depth		=	0;
	int			color_type		=	0;
	int			interlace_type	=	0;

	png_structp	pPng		=	NULL;
	png_infop	pPngInfo	=	NULL;

#if dUsePallete
	//パレット情報用変数
	int			numOfColorInPalette	=	0;		//色数
	png_colorp	pColorPalette		=	NULL;		//PNGパレット

	//tRNS情報用変数(indexカラーの場合は、tRNSは透明度を示す)
	int			numOfTRNSValue	=	0;
	png_bytep	pTRNSValues		=	NULL;
#endif

	FILE			*fp			=	NULL;
	dat_png_buffer	png_buff	=	{0};
	struct stat		filestat	=	{0};

	LOGI("createPNGFromDatPath:%s", filePath);

	if (stat (filePath, &filestat))
	{
		LOGI("createPNGFromDatPath stat pngfile");
		return;
	}
	png_buff.data_len	=	filestat.st_size;

	LOGI ("png_buff.data_len=%lu", png_buff.data_len);

	fp	=	fopen(filePath, "rb");
	if (!fp)
	{
		LOGE("createPNGFromDatPath Can't open file(%s)", filePath);
		return;
	}

	png_buff.data	=	(unsigned char *)calloc (png_buff.data_len, 1);

	fread(png_buff.data, 1, png_buff.data_len, fp);
	fclose(fp);

	png_buff.data_offset	=	8;

	pPng	=	png_create_read_struct (PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	if (!pPng)
	{
		LOGE ("createPNGFromDatPath png_create_read_struct: failed");
		goto endproc;
	}

	pPngInfo	=	png_create_info_struct (pPng);
	if (!pPngInfo)
	{
		LOGE ("createPNGFromDatPath png_create_info_struct: failed");
		goto endproc;
	}

	png_set_read_fn (pPng, (void *) &png_buff, png_memread_func);

	png_set_sig_bytes (pPng, 8);

	//画像情報取得
	png_read_info (pPng, pPngInfo);

	png_get_IHDR (pPng, pPngInfo, &width, &height, &bit_depth, &color_type, &interlace_type, NULL, NULL);

#if dUsePallete
	//パレットがあれば、パレットを読み込む（PNGパレットは、libpng側で開放する）
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//PNGパレットを読み込む
		png_get_PLTE (pPng, pPngInfo, &pColorPalette, &numOfColorInPalette);
		png_get_tRNS (pPng, pPngInfo, &pTRNSValues, &numOfTRNSValue, NULL);
	}
#endif
	LOGI("width:%d height:%d", width, height);

	imageSize = width * height * 4;

	//画像データ用バッファーの確保
	pDataBuffer	=	(unsigned char *)calloc (imageSize, sizeof (unsigned char));
	if (!pDataBuffer)
	{
		// メモリ不足
		LOGE("createPNGDataFromPath malloc pDataBuffer: failed");
		goto endproc;
	}

	// 各画像情報を格納
	imageInfo->width	=	width;
	imageInfo->height	=	height;
	imageInfo->bitdepth =	bit_depth;
#if dUsePallete
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//念のため8を入れておく
		imageInfo->bitdepth	=	8;
	}
#endif
	imageInfo->channel	=	4;
	imageInfo->size		=	imageSize;

#if dUsePallete
	if (color_type == PNG_COLOR_TYPE_PALETTE)
	{
		//インデックスカラーの時の処理
		png_size_t	rowBytes	=	png_get_rowbytes (pPng, pPngInfo);
		png_uint_32 rowIndex	=	0;
		int			rowOffset	=	0;

		unsigned char	*pLineBuffer	=	(unsigned char *)calloc (rowBytes, sizeof(unsigned char));
		if (!pLineBuffer)
		{
			LOGE("createPNGDataFromPath malloc pLineBuffer: failed");
			goto endproc;
		}

		//一行ごと、各ピクセルのパレットに対するインデックス値を取得し、RGBAに展開していく。
		while (rowIndex < height)
		{
			png_read_row (pPng, pLineBuffer, NULL);

			for (int idxInLine = 0; idxInLine < width; idxInLine++)
			{
				unsigned char	palletIndex		=	0;

				switch (bit_depth)
				{
					case 1:	//1bitでインデックスを表す?
					{
						const static char sBitDepth1Mask[8]	=	{
								static_cast<char> (0x80),
								static_cast<char> (0x40),
								static_cast<char> (0x20),
								static_cast<char> (0x10),
								static_cast<char> (0x8),
								static_cast<char> (0x4),
								static_cast<char> (0x2),
								static_cast<char> (0x1)
						};

						int				bufIndex	=	(idxInLine / 8);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 8);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	7 - maskIndex;				//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth1Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;

					case 2:	//2bitでインデックスを表す?
					{
						const static char sBitDepth2Mask[4]	=	{
								static_cast<char> (0xC0),
								static_cast<char> (0x30),
								static_cast<char> (0xC),
								static_cast<char> (0x3),
						};

						int				bufIndex	=	(idxInLine / 4);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 4);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	6 - (maskIndex * 2);		//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth2Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;
					case 4:	//4bitでインデックスを表す?
					{
						const static char sBitDepth4Mask[2]	=	{
								static_cast<char> (0xF0),
								static_cast<char> (0xF),
						};

						int				bufIndex	=	(idxInLine / 2);			//バイトデータの取得先インデックスを算出
						int				maskIndex	=	(idxInLine % 2);			//バイトデータからビットデータ算出用のマスクのインデックスを算出
						int				bitOffset	=	4 - (maskIndex * 4);		//バイトデータからビットデータ算出用のシフト数を算出
						unsigned char	byteData	=	*(pLineBuffer + bufIndex);	//バイトデータを取得

						byteData	&=	sBitDepth4Mask[maskIndex];

						palletIndex	=	(byteData >> bitOffset);
					}
						break;

					case 8:
						palletIndex	=	*(pLineBuffer + idxInLine);
						break;
				}

				png_color	*colorData		=	pColorPalette + palletIndex;
				int			posInImageBuf	=	rowOffset + (idxInLine * 4);

				*(pDataBuffer + posInImageBuf + 0)	=	colorData->red;
				*(pDataBuffer + posInImageBuf + 1)	=	colorData->green;
				*(pDataBuffer + posInImageBuf + 2)	=	colorData->blue;
				*(pDataBuffer + posInImageBuf + 3)	=	(palletIndex < numOfTRNSValue ? *(pTRNSValues + palletIndex) : 0xFF);
			}

			rowOffset += imageInfo->width * 4;
			rowIndex++;
		}
		free (pLineBuffer);
	}
	else
#endif
		// 1行づつ読み込む
	{
		png_uint_32 i		=	0;
		int			offset	=	0;

		while (i < height)
		{
			png_read_row (pPng, pDataBuffer + offset, NULL);

			offset	+=	imageInfo->width * 4;
			i++;
		}
	}

	// 読み込み終了
	png_read_end (pPng, NULL);

	// データを返却
	*ppData	=	pDataBuffer;

	endproc:
	// clean up.
	png_destroy_read_struct (&pPng, &pPngInfo, NULL);

	if (png_buff.data)
		free (png_buff.data);
}

// ------------------------------
// Function
// ------------------------------
/**
 * ファイル読み込みのコールバック関数
 */
void
PngUtils::pngReadFileCallback(png_structp pPng, png_bytep pBuf, png_size_t len)
{
    // この関数でpng_set_read_fnで指定したAssetが取得できる。
    AAsset *pAsset = (AAsset*)png_get_io_ptr(pPng);
#ifndef LIBPNGDEBUG_MACOSX
    AAsset_read(pAsset, pBuf, len);
#else
	fread (pBuf, len, 1, pAsset);
#endif
}

/**
 * ファイル読み込みのコールバック関数
 */
void
PngUtils::png_memread_func(png_structp png_ptr, png_bytep buf, png_size_t size)
{
    dat_png_buffer *png_buff = (dat_png_buffer *)png_get_io_ptr(png_ptr);
    if (png_buff->data_offset + size <= png_buff->data_len)
    {
        memcpy(buf, png_buff->data + png_buff->data_offset, size);
        png_buff->data_offset += size;
    } else {
        png_error(png_ptr,"png_mem_read_func failed");
    }
}



