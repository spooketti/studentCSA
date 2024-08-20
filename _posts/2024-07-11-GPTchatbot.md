---
layout: post
toc: false
comments: false
title: GPT chatbot with java spring backend
description: example frontend for gpt chatbot, making calls to spring_portfolio backend
courses: { csa: {week: 3}  }
permalink: /GPT_overview/chatbot_example
menu: nav/GPT_overview.html
type: ccc
---


<html lang="en">
<head>
<title>AI Chat Bot</title>
<!-- <link rel="stylesheet" href="/portfolio_2025/assets/css/chatbot.css"> -->
 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
 <meta charset="utf-8">
</head>
<body>
<section class="msger">
  <header class="msger-header">
    <div class="container-profile">
      <div class="summary-row">
        <div class="sumText">
          <h1 id="initName"></h1>
          <input type="hidden" id="initId">
          <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        </div>
      </div>
      <br>
    </div>
    <!-- header, contains chat history retrieve button and delete chat button-->
    <div class="msger-header-title">
      <i class="fa fa-comment" title="AI Chat Bot"></i> AI Chat Bot
    </div>
    <div id="waiting" class="msger-header-options" style="display:none">
      <span><i class="fa fa-cog fa-spin" title="Waiting for response..."></i></span>
    </div>
    <div class="msger-header-options" >
      <span id="retieve_chat_history" style="cursor: pointer;"><i class="fa fa-history" title="Retrieve Chat History..."></i></span>
      <span style="width: 10px;display: inline-block;">&nbsp;</span>
      <span id="delete_chat" style="cursor: pointer;"><i class="fa fa-trash" title="Delete Chat History..."></i></span>
    </div>  
  </header>

  <main class="msger-chat">
    <div class="msg left-msg">
      <div
       class="msg-img"
       style="background-image: url(/portfolio_2025/assets/icons/icons8-chat-bot-64.png); width: 64px;
  height: 64px;" title="AI Bot"
      ></div>
      <div class="msg-bubble">
        <div class="msg-info">
          <div class="msg-info-name">Chat Bot</div>
        </div>
        <div class="msg-text">
          Hi, welcome to AI Chat Bot! Go ahead and send me a message. 😄
        </div>
      </div>
    </div>
  </main>



  <!-- area for submitting the chat response-->
  <form class="msger-inputarea">
    <input type="text" class="msger-input" placeholder="Enter your message...">
    <button type="submit" id = "msger-send-btn" class="msger-send-btn">Send</button>
  </form>
  <button id="toggle-response-btn" class = "toggle-response-btn">Toggle Response type (instant or streamed)</button>
</section>
</body>



<script type="text/javascript" src="/portfolio_2025/assets/js/chatbot.js">
</script>
</html>