# DownloadTO


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** |  | [default to undefined]
**filename** | **string** |  | [default to undefined]
**filesize** | **string** |  | [default to undefined]
**status** | [**DownloadState**](DownloadState.md) |  | [optional] [default to undefined]
**statusMessage** | **string** |  | [optional] [default to undefined]
**progress** | **number** |  | [optional] [default to undefined]
**averageSpeed** | **string** |  | [optional] [default to undefined]
**timeRemaining** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { DownloadTO } from './api';

const instance: DownloadTO = {
    id,
    filename,
    filesize,
    status,
    statusMessage,
    progress,
    averageSpeed,
    timeRemaining,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
