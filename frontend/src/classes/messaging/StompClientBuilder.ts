import type {IMessage} from "@stomp/stompjs"
import {Client} from '@stomp/stompjs'

class StompClientBuilder {
    private readonly factoryID: number;
    private client: Client;

    constructor(factoryID: number) {
        this.factoryID = factoryID;
        this.client = new Client({
            brokerURL: 'http://localhost:8080/stompbroker/',
            connectHeaders: {
                login: 'your-username', //need security-config in backend first
                passcode: 'your-password',
            },
            debug: (str: string) => {
                console.log('STOMP: ' + str);
            },
            onConnect: () => {
                this.subscribeToTopic(this.factoryID);
            },
            onStompError: (frame: any) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
        });
    }

    private subscribeToTopic(factoryID: number): void {
        this.client.subscribe("/info/factory/" + factoryID, (message: IMessage) => {
            console.log('Received:', message.body);
        });
    }

    public activate(): void {
        this.client.activate();
    }

    public deactivate(): void {
        this.client.deactivate();
    }
}

export default StompClientBuilder;
