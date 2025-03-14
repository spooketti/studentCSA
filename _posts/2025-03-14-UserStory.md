---
layout: post
toc: true
title: User Experience of using Mortevision
description: User experience for Mortevision
type: ccc
courses: {csa: {week: 20}}
comments: True
---
<style>
    .reformatimg
    {
        width:40%;
    }
</style>
# Mortevision User Experience (User Story)

The Mortevision service on the Nighthawk Coders website utilizes a WebSocket system for signaling, and WebRTC to send media streams across our AWS Webserver. But how does this work on the user side?<br>
<h2>How do I use Mortevision?</h2>
To begin, Mortevision has two types of clients, the streaming client and the watcher client.
<br>
<img src="{{site.baseurl}}/images/userstory/manual.png" class="reformatimg">
<br>
To begin a broadcast, click upon the broadcast button and select what to share
<br>
<img src="{{site.baseurl}}/images/userstory/stream.png" class="reformatimg">
<br>
That's it for the streamer!
The media shared with the browser is now ready to be shared with clients to watch the stream!

<h2>Mortevision Watcher Client</h2>
Because of how web standards work, videos (such as Mortevision's video stream) and audio cannot autoplay without the user having interacted first.
<br>
To circumvent this, first click the website a little bit to make sure the video can play.
Then click upon the watcher button, and the broadcast will be streamed onto the client computer.
<br>
<img src="{{site.baseurl}}/images/userstory/watch.png" class="reformatimg">
<br>

## That's The Mortevision Experience!
