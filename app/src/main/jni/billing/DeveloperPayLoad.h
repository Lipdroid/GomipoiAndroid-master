/*
 * DeveloperPayLoad.h
 *
 *  Created on: 2014/09/01
 *      Author: kazuya
 */

#ifndef DEVELOPERPAYLOAD_H_
#define DEVELOPERPAYLOAD_H_

#include <string.h>
#ifdef __cplusplus
extern "C"
{
#endif

/**
 * ペイロードの作成
 * md5 (sku + ごみ)
 */
void createDeveloperPayLoad(char *payload, const char* productId,
		const char *productSolt);

#ifdef __cplusplus
}
#endif

#endif /* DEVELOPERPAYLOAD_H_ */
