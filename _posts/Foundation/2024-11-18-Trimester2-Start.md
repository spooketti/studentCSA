---
layout: post
toc: true
title: Start of Trimester 2
description: Recap of Trimester 1 and MCQ
type: ccc
courses: {csa: {week: 8}}
comments: True
---

# Start of Trimester 2 AP CSA

## My Project
Our class last trimester split up into various themes for projects, one side related to fun and games, the other related to making useful tools for our teacher, John Mortenson. Our projects theme was developing tools, and on our ProductivityFrontend branch, our team's project was...
<br>

# Mortevision
One aspect of our lives in AP CSA is doing presentations on our work. 
In that process there is often a lot of clutter in who has presented; cramming around one of three televisions; and it being an overall unclean process. 
<br>
That is why my team, JITS, developed Mortevision.

Our team is comprised of Ian Wu, Srijan Atti, Kayden Le, Tarun Jaikumar, and me.
Mortevision is a tool designed to help with this process, and my task was to develop the presenting system, or screen share.
One thing I've always disliked was having to cram around 12 other people to look at one tv screen, when I could be at my desk and just watch the stream from there. 

So that's what I developed, a livestream system using WebRTC to allow others to watch a stream from their desks and allow for easy connections with no third party like Ditto required.

<img src="{{site.baseurl}}/images/post/2024-11-18/0.png">

The source code is as follows
```js
let peerConnection

async function startStream()
{
    let stream
    try
    {
        stream = await captureScreen()
    }
    catch
    {
        console.error("can u js be normal man")
    }
    if (!peerConnection) {
        peerConnection = createPeerConnection();
    }
    stream.getTracks().forEach((track) => peerConnection.addTrack(track, stream)); //add media stream to each connected peer
}

async function viewStream()
{
    if(!peerConnection)
    {
        peerConnection = createPeerConnection()
    }
    if(peerConnection.getTransceivers().length === 0)
    {
    peerConnection.addTransceiver("video", { direction: "recvonly" }) //recieve only
    peerConnection.addTransceiver("audio", { direction: "recvonly" })
    document.getElementById("streamOffline").style.display = "none"
    document.getElementById("mortStream").style.display = "block"
    }

    await peerNegotiation()
}

async function captureScreen() {
    let mediaStream = null;
    try {
        mediaStream = await navigator.mediaDevices.getDisplayMedia({
            video: {
                cursor: "always"
            },
            audio: true
        }); //get user video and audio as a media stream
        document.getElementById("streamOffline").style.display = "none"
        document.getElementById("mortStream").style.display = "block"
        document.getElementById("mortStream").srcObject = mediaStream
        return mediaStream;
    } catch (ex) {
        console.log("Error occurred", ex);
    }
}


function createPeerConnection()
{
    const peer = new RTCPeerConnection(servers)

    peer.addTransceiver('video', {
        direction: 'recvonly'
      });
      peer.addTransceiver('audio', {
        direction: 'recvonly'
      });

    peer.onnegotiationneeded = () => peerNegotiation()
    peer.onicecandidate = ({ candidate }) => {
        if (candidate && signalingServer.readyState === signalingServer.OPEN) {
          signalingServer.send(JSON.stringify({ target: id, type: 'candidate', payload: candidate }));
        }
      };
    peer.ontrack = ({streams}) => {
        document.getElementById("mortStream").srcObject = streams[0]
        document.getElementById("mortStream").style.display = "block"
        document.getElementById("streamOffline").style.display = "none"
    }
    return peer
}


async function peerNegotiation()
{
    if(!peerConnection)
    {
        peerConnection = createPeerConnection()
    }
    const offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);
    signalingServer.send(JSON.stringify({ target: id, type: 'offer', payload: offer }));

}

let id
signalingServer.onmessage = async(message) => {
    const data = JSON.parse(message.data)
    console.log(data.type)
    switch(data.type)
    {
        case "init":
            id = data.id
        break;

        case "offer":
            if (!peerConnection) {
                peerConnection = createPeerConnection();
            }
            await peerConnection.setRemoteDescription(new RTCSessionDescription(data.payload)); // Ensure remote description is set
            const answer = await peerConnection.createAnswer(); // Create answer only after setting remote description
            await peerConnection.setLocalDescription(answer); // Set the local description
            signalingServer.send(JSON.stringify({ target: data.payload.id, type: 'answer', payload: answer }));
            // signalingServer.send(JSON.stringify({description: peerConnection.localDescription}));
        break;

        case "answer":
            if (!peerConnection) {
                peerConnection = createPeerConnection();
            }
            await peerConnection.setRemoteDescription(new RTCSessionDescription(data.payload));
        break;

        case "candidate":
            if (!peerConnection) {
                peerConnection = createPeerConnection();
            }
            await peerConnection.addIceCandidate(new RTCIceCandidate(data.payload));
        break;
    }
}

function toggleBroadcastButton(isVisible)
{
    if(isVisible)
    {
        document.getElementById("broadcastButton").style.display = "flex"
        return
    }
    else
    {
        document.getElementById("broadcastButton").style.display = "none"
    }
}
```
There are a few other scripts but for the most part they do not matter.

Before I breakdown the code, let's talk about how this works fundamentally.
<br>
<img src="https://www.techtarget.com/rms/onlineimages/how_webrtc_works-f_mobile.png">
<br>
A peer represents a client or a computer connected to the stream.
WebRTC creates a real time connection between these peers by directly sending the data (video and audio) between the two peers. In order to send these data streams, we need to actually know what peer or ip address to send it to. To do this each user will use a STUN server, which I used the ones publicly available by Google, to find their public IP address. Then once a user clicks the broadcast button, it sends a signal to the signaling server. Skipping some information, the signaling server is the Java backend in which the two users communicate with each other on signaling information such as someone joining the stream, starting a broadcast, and other events if necessary. 

But what is the-

# Signaling Server

The WebRTC connection needs a middle man to resolve how to connect them together. The signaling server serves this purpose by sending events between the two clients through the usage of WebSocket, which allows for real time events. 
These events are typically called when creating a peer negotation or peer connection. 

Here's a look into what this looks like on the backend itself.

Config
```java
package com.nighthawk.spring_portfolio.config;  

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SignalingHandler(), "/socket")
          .setAllowedOrigins("*");
          System.out.println("why");
    }
}
```

This script will create a /socket endpoint we can send requests to on our HTML front end given our ws:// local address
For the sake of ease of testing, the allowed origins is set to all, but in production it would be set to the nighthawkcoders domain. 

Signaling Handler
```java
package com.nighthawk.spring_portfolio.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class SignalingHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession peerSession : sessions) {
            if (!peerSession.getId().equals(session.getId()) && peerSession.isOpen()) {
                peerSession.sendMessage(message); 
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
```

This simple script keeps tracks of what "sessions" there are, essentially representing each connected user. 
If a user joins they will be added to the ArrayList, and removed when the leave.
The most important block of code is the handleTextMessage() method, as it iterates through the connected users and sends them information so long as they themselves are not the sender, such as joining or broadcasting.

# Night At The Museum


| **Assignment**                | **Points**    | **Grade** | **Evidence** |
|-------------------------------|---------------|-----------|--------------|
| Sprint 1-3 Review Ticket      | 3             |           |  [Sprint 1 Review Ticket](https://spooketti.github.io/studentCSA//2024/09/08/checklistSprintOne_IPYNB_2_.html) <br> [Sprint 2 Review Ticket](https://github.com/spooketti/studentCSA/issues/5) <br> [Sprint 3 Review Ticket](https://spooketti.github.io/studentCSA//2024/11/18/Trimester2-Start.html) <br> [Sprint 1 Bonus](https://github.com/spooketti/studentCSA/issues/2)          |
| Sprint 3 Team Issue(s)/Plan   | 2             |        |         [To Do List](https://github.com/CSA-Coders-2025/Planning-Repository-Issue-House-/issues/16) <br> [Team Plan](https://github.com/JITS-K/JITS_Frontend_Project1/issues/1)        |
| Beginning-2-End Contribution  | 2             |           |              |
| N@tM Team Presentation        | 2             |           |              |
| Live Review Indi Demo         | 1             |           |              |
| **Total**                     | 10            |           |              |

| **Skill**                  | **Points**    | **Grade** | **Evidence** |
|----------------------------|---------------|-----------|--------------|
| Work Habits (Analytics)    | 1             |           |  ![analytics]({{site.baseurl}}/images/post/2024-11-18/analytics.png)          |
| Evidence of Role in Team   | 1             |           |   ![progress in group chat]({{site.baseurl}}/images/post/2024-11-18/teamwork1.png) ![more progress]({{site.baseurl}}/images/post/2024-11-18/teamwork2.png)       | 
| Function / Purpose Design  | 1             |           |              | 
| Live Review                | 2             |           |              | 
| **Total**                  | 5             |           |              |


