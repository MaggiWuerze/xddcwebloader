# SearchControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**listSearchProviders**](#listsearchproviders) | **GET** /api/v1/search/ | |
|[**searchWithProvider**](#searchwithprovider) | **GET** /api/v1/search/{providerName}/{query} | |
|[**startDownloadFromSearchResult**](#startdownloadfromsearchresult) | **POST** /api/v1/search/ | |

# **listSearchProviders**
> Array<SearchEngineTO> listSearchProviders()


### Example

```typescript
import {
    SearchControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SearchControllerApi(configuration);

const { status, data } = await apiInstance.listSearchProviders();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<SearchEngineTO>**

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

# **searchWithProvider**
> Array<SearchResultItem> searchWithProvider()


### Example

```typescript
import {
    SearchControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SearchControllerApi(configuration);

let providerName: string; // (default to undefined)
let query: string; // (default to undefined)

const { status, data } = await apiInstance.searchWithProvider(
    providerName,
    query
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **providerName** | [**string**] |  | defaults to undefined|
| **query** | [**string**] |  | defaults to undefined|


### Return type

**Array<SearchResultItem>**

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

# **startDownloadFromSearchResult**
> DownloadTO startDownloadFromSearchResult(searchResultItem)


### Example

```typescript
import {
    SearchControllerApi,
    Configuration,
    SearchResultItem
} from './api';

const configuration = new Configuration();
const apiInstance = new SearchControllerApi(configuration);

let searchResultItem: SearchResultItem; //

const { status, data } = await apiInstance.startDownloadFromSearchResult(
    searchResultItem
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **searchResultItem** | **SearchResultItem**|  | |


### Return type

**DownloadTO**

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

