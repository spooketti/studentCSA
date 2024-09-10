---
layout: base
title: Student Home 
description: Home Page
hide: true
---
<link rel="stylesheet" href="style/home.css">
<div id="paralaxWrapper">
<div id="paralaxName" class="slideable" data-slideanim="slideLeft">Jonathan Liu</div>
<i id="paralaxSubtitle">Software Engineer Student: AP CSA Portfolio</i>
<img src="images/homepara/sunsetpara.png">
<img src="images/homepara/roadpara.png" id="roadpara" style="z-index:2">
</div>
<h1 class="slideable" data-slideanim="slideOpacity">Welcome to my AP Computer Science Applications Portfolio!</h1>
<p class="slideable" data-slideanim="slideLeft">I am Jonathan Liu, a junior in high school and this is my website for my AP CSA class. On this website you will find my projects, write-ups, and much more to come! I am interested in computer science, game development, 3D modeling and cadding, eletrical engineering with Arduino, and much more.</p>

<h1 class="slideable" data-slideanim="slideRight">My Projects</h1>
<div id="linuxTerminal">
<pre id="linuxPre">
<span class="linuxGreen">jonathan@liu</span>:<span class="linuxBlue">/studentCSA</span><span id="linux1">$</span>
</pre>
</div>

<div>



</div>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://raw.githubusercontent.com/darcyclarke/Repo.js/master/repo.min.js"></script>
<script>
   $(function() {
        $('body').repo({ user: 'spooketti', name: 'Relay' })
    });
    </script>
<script src="assets/js/homeParalax.js"></script>    
<script src="assets/js/homeLinuxEffect.js"></script>    