/*
 * DeveloperPayLoad.c
 *
 *  Created on: 2014/09/01
 *      Author: kazuya
 */

#include <stdio.h>
#include "md5.h"
#include "DeveloperPayLoad.h"

void strToMD5(char *payload, const char* soltProductId);

/**
 * ペイロードの作成
 * md5 (sku + ごみ)
 */
void createDeveloperPayLoad(char *payload, const char* productId,
		const char *productSolt)
{
	if (payload == NULL || productId == NULL)
	{
		return;
	}

	int productIdLength = strlen(productId);
	int soltLength = strlen(productSolt);
	int soltProductIdLength = productIdLength + soltLength;

//	char *soltProductId = (char *) malloc(sizeof(char) * soltProductIdLength);
    char soltProductIdArray[32];
    char *soltProductId = &soltProductIdArray;
	memset(soltProductId, 0, sizeof(char) * soltProductIdLength);
	// ごみ混入
	strcat(soltProductId, productId);
	strcat(soltProductId, productSolt);
	// MD5化
	strToMD5(payload, soltProductId);
//	free(soltProductId);
}

/**
 * md5文字列化
 */
void strToMD5(char *payload, const char* soltProductId)
{
	md5_state_t md5;
	md5_init(&md5);

	md5_append(&md5, (md5_byte_t *) soltProductId, strlen(soltProductId));

	md5_byte_t digest[16];
	md5_finish(&md5, digest);
	int n;
	for (n = 0; n < 16; ++n)
	{
		snprintf(&(payload[n * 2]), 16 * 2, "%02x", (unsigned int) digest[n]);
	}
}
