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
    }
</style>
<div id="aboutWrapper">
<h1>About Jonathan Liu</h1>
<img src="{{site.baseurl}}/images/me.jpg" id="AboutMePhoto">
<p>Hello! I'm Jonathan Liu and am a passionate computer science student, and this is my website for AP CSA! I began programming around the sixth grade after I had retired my piano lessons for something I found more passion in. I had always loved video games, and admittedly imagined everything as being a video game. When I was a young kid, maybe 6 or so, I wouldn't play with toy cars corrctly, and would move them sideways pretending they were video game characters. I broke a lot of toy cars that way... sorry Mom and Dad. One thing almost everyone in my generation has done while riding in a car, is imagining a ninja parkouring across the cars on the highway while looking out a window. That passion for games finally came to fruition with my development of Unity games. They were not very polished, but they were a start! I then began learning Python and Node.js in my free time using Discord bots and lua through Roblox. In highschool I then grew my passion for web development out with HTML, JavaScript, CSS, and Python with SQL. I began doing various computer science related projects, Java FRC Robotics team, Arduino electrical engineering, Java Fabric Minecraft mods, CTFs, and more to come! Outside of my love for computers, I like to play guitar, both acoustic and electrical and I haven't ever given up piano, only lessons. I have self taught myself guitar and I'm currently writing an original song on guitar!  
I play badminton for fun, like doing 3D art, and love biking with my friends.</p>
</div>
