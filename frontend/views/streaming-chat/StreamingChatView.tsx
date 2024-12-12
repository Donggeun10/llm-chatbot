import {useState} from "react";
import {v4 as uuidv4} from 'uuid';

import {MessageInput, MessageList, MessageListItem} from "@vaadin/react-components";
import {ChatService} from "../../generated/endpoints";


export default function StreamingChatView() {
    const [messages, setMessages] = useState<MessageListItem[]>([]);
    const uuid = uuidv4();

    function addMessage(message: MessageListItem) {
        setMessages(messages => [...messages, message]);
    }

    function appendToLastMessage(chunk: string) {
        setMessages(messages => {
            const lastMessage = messages[messages.length - 1];
            lastMessage.text += chunk;
            return [...messages.slice(0, -1), lastMessage];
        });
    }

    async function sendMessage(message: string) {
        addMessage({
            text: message,
            userName: 'U'
        });

        let first = true;
        ChatService.chatStream(uuid, message).onNext(chunk => {
            if (first && chunk) {
                addMessage({
                    text: chunk,
                    userName: 'Assistant'
                });

                first = false;
            } else {
                appendToLastMessage(chunk);
            }
        });
    }

    return (
        <div className="p-m flex flex-col h-full box-border">
            <MessageList items={messages} className="flex-grow"/>
            <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
        </div>
    );
}
