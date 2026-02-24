// frontend/src/api/stomp/downloadUpdatesClient.ts
import {Client, IMessage, StompSubscription} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {EnvironmentService} from "../../data/environmentService";

export type DownloadSocketPayload = {
    id: string;
    [key: string]: unknown;
};

export type DownloadEventType = 'new' | 'update' | 'delete' | 'cancel';

export type DownloadSocketEvent<T = DownloadSocketPayload> = {
    type: DownloadEventType;
    payload: T;
};

const baseUrl = EnvironmentService.getWSBaseUrl();

type SubscriptionHandle = { unsubscribe: () => void };

class DownloadUpdatesClient {
    private client: Client | null = null;
    private subscriptions = new Map<string, StompSubscription>();
    private connected = false;

    connect() {
        if (this.client) return;

        console.log(EnvironmentService.getEnvironment())

        this.client = new Client({
            //webSocketFactory: () => new SockJS(baseUrl+'/downloads'),
            webSocketFactory: () => new SockJS(baseUrl),
            reconnectDelay: 5000,
            heartbeatIncoming: 10000,
            heartbeatOutgoing: 10000,
            debug: () => {
            },
            onConnect: () => {
                this.connected = true;
            },
            onDisconnect: () => {
                this.connected = false;
            },
            onWebSocketClose: () => {
                this.connected = false;
            },
        });

        this.client.activate();
    }

    disconnect() {
        if (!this.client) return;

        for (const [, sub] of this.subscriptions) sub.unsubscribe();
        this.subscriptions.clear();

        this.client.deactivate();
        this.client = null;
        this.connected = false;
    }

    subscribe<T>(
        key: string,
        destination: string,
        onMessage: (data: T, raw: IMessage) => void,
    ): SubscriptionHandle {
        this.connect();

        const existing = this.subscriptions.get(key);
        if (existing) {
            existing.unsubscribe();
            this.subscriptions.delete(key);
        }

        const waitUntilConnected = () => {
            if (!this.client) return;
            if (!this.connected) {
                window.setTimeout(waitUntilConnected, 100);
                return;
            }

            const sub = this.client.subscribe(destination, (msg: IMessage) => {
                try {
                    const parsed = JSON.parse(msg.body) as T;
                    onMessage(parsed, msg);
                } catch {
                    // non-JSON bodies can be handled here if needed
                }
            });

            this.subscriptions.set(key, sub);
        };

        waitUntilConnected();

        return {
            unsubscribe: () => {
                const sub = this.subscriptions.get(key);
                if (sub) sub.unsubscribe();
                this.subscriptions.delete(key);
            },
        };
    }

    subscribeDownloadEvents(
        keyPrefix: string,
        onEvent: (event: DownloadSocketEvent) => void,
    ): SubscriptionHandle {
        const subs: SubscriptionHandle[] = [];

        subs.push(
            this.subscribe<DownloadSocketPayload>(`${keyPrefix}-new`, '/topic/newDownload', payload => {
                onEvent({type: 'new', payload});
            }),
        );

        subs.push(
            this.subscribe<DownloadSocketPayload>(`${keyPrefix}-update`, '/topic/updateDownload', payload => {
                onEvent({type: 'update', payload});
            }),
        );

        subs.push(
            this.subscribe<DownloadSocketPayload>(`${keyPrefix}-delete`, '/topic/deleteDownload', payload => {
                onEvent({type: 'delete', payload});
            }),
        );

        subs.push(
            this.subscribe<DownloadSocketPayload>(`${keyPrefix}-cancel`, '/topic/cancelDownload', payload => {
                onEvent({type: 'cancel', payload});
            }),
        );

        return {
            unsubscribe: () => subs.forEach(s => s.unsubscribe()),
        };
    }
}

export const downloadUpdatesClient = new DownloadUpdatesClient();