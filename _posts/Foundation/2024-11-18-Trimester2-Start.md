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

<style>
    td img
    {
        width:75%;
    }
</style>

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
| Sprint 3 Team Issue(s)/Plan   | 2             |        |          [Team Plan](https://github.com/JITS-K/JITS_Frontend_Project1/issues/1)        |
| Beginning-2-End Contribution  | 2             |           | [Ideation](https://github.com/JITS-K/JITS_Frontend_Project1/issues/1) <br> [To Do List](https://github.com/CSA-Coders-2025/Planning-Repository-Issue-House-/issues/16) <br> ![Design 1]({{site.baseurl}}/images/post/2024-11-18/design1.png) <br> ![Work]({{site.baseurl}}/images/post/2024-11-18/contrib.png) <br>  ![Work]({{site.baseurl}}/images/post/2024-11-18/videophase.png)     |
| N@tM Team Presentation        | 2             |           |     ![Team Present]({{site.baseurl}}/images/post/2024-11-18/feedback.png)         |
| Live Review Indi Demo         | 1             |           |       See Above       |
| **Total**                     | 10            |           |              |

| **Skill**                  | **Points**    | **Grade** | **Evidence** |
|----------------------------|---------------|-----------|--------------|
| Work Habits (Analytics)    | 1             |           |  ![analytics]({{site.baseurl}}/images/post/2024-11-18/analytics.png) ![commit1]({{site.baseurl}}/images/post/2024-11-18/commit1.png) ![commit2]({{site.baseurl}}/images/post/2024-11-18/commit2.png) ![commit3]({{site.baseurl}}/images/post/2024-11-18/commit3.png)     |
| Evidence of Role in Team   | 1             |           |   ![progress in group chat]({{site.baseurl}}/images/post/2024-11-18/teamwork1.png) ![more progress]({{site.baseurl}}/images/post/2024-11-18/teamwork2.png)       | 
| Function / Purpose Design  | 1             |           |      ![function]({{site.baseurl}}/images/post/2024-11-18/function.png)         | 
| Live Review                | 2             |           |              | 
| **Total**                  | 5             |           |              |

# Collegeboard Practice MCQ 
Score: 19/40

So it's not the greatest score for practice, so going forward I'll focus on getting things right more than getting things done...

## Corrections
Q40
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/002673dc81c296f0f64de524352d075f/original.jpg">
C) The recursive call isn't printed before the whatsItDo(temp), so it is going forward printing lines

Q39
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/fda54504a6670d2190b0cfd89d3fcfaa/original.jpg">
D) recur(9) is 18, the progression should have continued with recur(18) so the correct progression should have been recur(recur(27/3)) -> recur(18) -> recur(recur(6)) -> recur(12) -> recur(recur(4)) -> recur(8) -> 16

Q38
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/501a69578482846725a76c54ab918279/original.jpg">
A) X is not a required variable if y is more than 10000, so A (y > 10000 | | x > 1000) && (y > 10000 | | x < 1500) is the right answer as it keeps x in the range with &&

Q37
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/5c09d75c258b4b3dabba2a8c33d3b1a8/original.jpg">
D) x < 6 and x < 7 will work as previously I only chose x < 6; however, x < 7 will still terminate the loop at the same time

Q36
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/1f3e0982f6c40e801d4d15a8316817ac/original.jpg">
C) 8, 9, and 11 will work because 8 % 2 ==0, 9 %2 != 0 and 9 is not greater than 9, so the else case will fire, and 11 > 9

Q35
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/5c4fd296b6cab7c9d7bf8967009969e8/original.jpg">
E) 4752 as for loop 1 result is 4, num is 257, loop 2 result 47 num 25, loop 3 result 475 num 2, loop 4 result 4752 num 0

Q34
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/5e0d6aca01edaef3dfc91e8ae330af40/original.jpg">
B) II only being center = new Point(a,b); radius = r; because I will not assign the center and center itself is private

Q33
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/0e16d20e2490230415bc3e38e043d265/original.jpg">
E) Infinite loop as k will always be < 4 because it never increases

Q30 <img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/322e49e63a36e812d997ab61f4ccb08a/original.jpg">
C) ilercom as it is getting the 5th value to the end, before chaining the beginning to the start to the 4th value to the end

Q26
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/da84118573a4b4480f15c55178b884f4/original.jpg">

A)<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/dd0bc407ea8e4ef1b8a4f1564e5bd79d/original.jpg">
This will work because it is iterating through each value in the array and seeing if the number % 2 is not equal to 0, meaning it has to be odd

Q25
D) I and II as solution III does not prove it can fit as surface area and volume are not enough

Q24
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/647dbaa080e6d83f57b035e56deb56fd/original.jpg">
D) 7 as the array contains { {1, 4, 7}, {2, 5, 8}, {3, 6, 9} }, and the 3rd value in the first column is 7

Q23
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/ed033b294ad1d99eb6ae4e37a1fe3cc9/original.jpg">
B) my initial mistake was I thought words that started with b were sent to the end as opposed to being inserted at animals.size() - k

Q22
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/c79fa4afed4476a54d785b4abdd5f148/original.jpg">
B) Line 5 will compile because it inherits the superclass's toString

Q21
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/c3745b0da0c90d3315a0931604c1efc2/original.jpg">
D)Math.abs (row [num] - val) < minDiff correctly completes the code as my answer relies on it being a for loop as opposed to a for each loop

Q20 <img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/10073d3e9af8bd104345e2b96674583b/original.jpg">
E) The numbers are flipped as j increases and k decreases

Q18
Assume that myList is an ArrayList that has been correctly constructed and populated with objects. Which of the following expressions produces a valid random index for myList?
Responses
B) (int) ( Math.random () * myList.size () ) is the correct answer as my original answer decreased the range by 1

Q16
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/7fcd53f95a149d73a904e9a98c286dc7/original.jpg">
D) My original answer once again decreases the range by 1

Q12
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/375a6484b5197c53df8f05c3ec50c9b6/original.jpg">
C) optr is the output as it gets every 2nd value and chains them together

Q11
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/a6bf889b2fe003ec68b291c4bf3d7b3a/original.jpg">
B) The answer if(last < 0){return -1} works as if it is <= 0 then it will return -1 instead of 0>

Q10
<img src="https://assets.learnosity.com/organisations/537/media.academicmerit.com/a6bf889b2fe003ec68b291c4bf3d7b3a/original.jpg">
B) II works as it is fine if the element only contains one element, but II will eventually cause an out of bounds exception




