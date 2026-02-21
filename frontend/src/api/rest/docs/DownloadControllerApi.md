# DownloadControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addDownload**](#adddownload) | **POST** /download/ | |
|[**failedDownloads**](#faileddownloads) | **GET** /download/failed | |
|[**getActiveDownloads**](#getactivedownloads) | **GET** /download/active/ | |
|[**getDownload**](#getdownload) | **GET** /download/{id} | |
|[**listDownloads**](#listdownloads) | **GET** /download/ | |
|[**removeDownload**](#removedownload) | **DELETE** /download/{id} | |

# **addDownload**
> Array<DownloadTO> addDownload(downloadFormTO)


### Example

```typescript
import {
    DownloadControllerApi,
    Configuration,
    DownloadFormTO
} from './api';

const configuration = new Configuration();
const apiInstance = new DownloadControllerApi(configuration);

let downloadFormTO: DownloadFormTO; //

const { status, data } = await apiInstance.addDownload(
    downloadFormTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **downloadFormTO** | **DownloadFormTO**|  | |


### Return type

**Array<DownloadTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **failedDownloads**
> Array<DownloadTO> failedDownloads()


### Example

```typescript
import {
    DownloadControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DownloadControllerApi(configuration);

const { status, data } = await apiInstance.failedDownloads();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<DownloadTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getActiveDownloads**
> Array<DownloadTO> getActiveDownloads()


### Example

```typescript
import {
    DownloadControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DownloadControllerApi(configuration);

let active: boolean; // (default to undefined)

const { status, data } = await apiInstance.getActiveDownloads(
    active
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **active** | [**boolean**] |  | defaults to undefined|


### Return type

**Array<DownloadTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getDownload**
> DownloadTO getDownload()


### Example

```typescript
import {
    DownloadControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DownloadControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getDownload(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**DownloadTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listDownloads**
> Array<DownloadTO> listDownloads()


### Example

```typescript
import {
    DownloadControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DownloadControllerApi(configuration);

const { status, data } = await apiInstance.listDownloads();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<DownloadTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeDownload**
> object removeDownload()


### Example

```typescript
import {
    DownloadControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DownloadControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.removeDownload(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

