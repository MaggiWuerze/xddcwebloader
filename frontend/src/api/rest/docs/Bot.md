# Bot


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** |  | [optional] [default to undefined]
**server** | [**Server**](Server.md) |  | [optional] [default to undefined]
**channel** | [**Channel**](Channel.md) |  | [optional] [default to undefined]
**name** | **string** |  | [optional] [default to undefined]
**pattern** | **string** |  | [optional] [default to undefined]
**creationDate** | **string** |  | [optional] [default to undefined]
**maxParallelDownloads** | **number** |  | [optional] [default to undefined]

## Example

```typescript
import { Bot } from './api';

const instance: Bot = {
    id,
    server,
    channel,
    name,
    pattern,
    creationDate,
    maxParallelDownloads,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
