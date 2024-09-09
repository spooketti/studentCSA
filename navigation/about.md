---
layout: page
title: About
permalink: /about/
---
<style>
    #AboutMePhoto
    {
    --angle: 0deg;
    border: 6px solid;
    border-image: conic-gradient(from var(--angle), rgb(8, 133, 229), rgb(3, 9, 191), rgb(83, 2, 134), rgb(59, 1, 96), rgb(63, 1, 70), rgb(8, 133, 229)) 1;
    animation: 3s rotate linear infinite;
    width:256px;
    }

@keyframes rotate {
    to {
      --angle: 360deg;
    }
  }
  
  @property --angle {
    syntax: '<angle>';
    initial-value: 0deg;
    inherits: false;
  }
    #aboutWrapper
    {
        display:flex;
        justify-content:center;
        flex-direction: column;
        align-items:center;
        width:100%;
    }
</style>
<div id="aboutWrapper">
<h1 class="slideable" data-slideanim="slideLeft">About Jonathan Liu</h1>
<img src="{{site.baseurl}}/images/me.jpg" id="AboutMePhoto" class="slideable" data-slideanim="slideOpacity">
<p class="slideable" data-slideanim="slideLeft">Hello! I'm Jonathan Liu and if you couldn't guess, I like coding</p>
<img src="{{site.baseurl}}/images/theboys.jpg" style="width:100%" class="slideable" data-slideanim="slideOpacity">
<span class="slideable" data-slideanim="slideRight">A photo of me and my friends going biking after one of the worst summers of my life, they're always there for me!</span>

<div style="width:100%; display:flex; gap:20px; justify-content:center" class="slideable" data-slideanim="slideRight">
<div style="width:240px">
<img src="https://www.worldatlas.com/upload/a6/ee/9d/shutterstock-104282006.jpg" style="width:100%" class="slideable" data-slideanim="slideLeft">
I myself have only visited China; however, both my parents had upbringings in China, with stories of how much harder their life was. My mother from 北京(Beijing) studied pathology and my father from 武汉(Wuhan) spent his days in middle-school soldering. His reasoning for going into medical was because he felt proficient in fixing machinery but not humans. They met in university and if my mom were to have never found a job in the United States, I would likely not exist as I am the younger sibling. 
</div>

<div style="width:240px" class="slideable" data-slideanim="slideLeft">
<img src="https://cdn.webshopapp.com/shops/94414/files/337529549/flag-of-connecticut.jpg" style="width:100%" class="slideable" data-slideanim="slideRight">
My mother was always a stickler about food, as when they originally moved into Iowa, she did not like the food options there. My mom and dad were willing to drive for miles across states just to find a Chinese restraunt and then drive back. Eventually my mother moved to Connecticut where I was born and I spent not much of my life here. But I hope to come back some day to see snow. 
</div>

<div style="width:240px" class="slideable" data-slideanim="slideLeft">
<img src="https://upload.wikimedia.org/wikipedia/commons/0/01/Flag_of_California.svg" style="width:100%" class="slideable" data-slideanim="slideRight">
Eventually my father found a job in the Bay Area where I have a majority of my memories. I lived a town called Foster City where I met some North California friends I still talk to regularly. After middle school ended I was prepared to arrive at San Mateo High School. My mom told me shortly of her new job at UCSD. I left everyone I had met at that point behind to start anew. In my freshman year of highschool I had no friends, and would regularly talk to my Chinese teacher as a means of friendship, and not before long she told me my next assignment was to go make a friend. Ouch, but in my sophmore year is when I met almost all of my friends that I still talk to today, except for a few exceptions (you know who you are).
</div>
</div>

<h1 class="slideable" data-slideanim="slideLeft">Ok, but who actually are you??</h1>
<p class="slideable" data-slideanim="slideRight">I'm Jonathan Liu, a 16 year old highschool student interested in software development</p>

<p class="slideable" data-slideanim="slideLeft">Some of my interests are:</p>
<ul>
<li class="slideable" data-slideanim="slideRight">Programming: Full Stack Web Development, Minecraft Modding (Fabric), Game Development, Python experiments with APIs (ML, Discord, OpenCV), Electrical engineering with Arduino, and more</li>
<li class="slideable" data-slideanim="slideRight">What Languages?: HTML, JavaScript, CSS, Java, Python, Arduino C++, Godot/Unity C#, Lua</li>
<li class="slideable" data-slideanim="slideRight">Game Dev: I like to make art and models in Blender, alongside 3D printing some of those models</li>
<li class="slideable" data-slideanim="slideRight">Music: I took piano lessons as a kid but now play independently, and self taught myself guitar, I've also been working on a song in recent times!</li>
<li class="slideable" data-slideanim="slideRight">Athletics? Ok I admit, I'm not a very athletic kid, but I love badminton, running, and biking with my friends</li>
<li class="slideable" data-slideanim="slideRight">Misc: I enjoy chess, cooking, gaming, and my dog 乐乐(lele)</li>