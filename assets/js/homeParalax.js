window.addEventListener("scroll",function(e)
{
    topDist = this.scrollY
    document.getElementById("paralaxWrapper").style.transform = `translate(0px,-${(topDist)}px)`
    document.getElementById("roadpara").style.transform = `translate(0px,-${(topDist)}px)`
    document.getElementById("paralaxName").style.transform = `translate(0px,${(topDist*1.5)}px)`
    document.getElementById("paralaxSubtitle").style.transform = `translate(0px,${(topDist*1.5)}px)`
})

let paraWrap = document.getElementById("paralaxWrapper")
document.body.style.paddingTop = (paraWrap.offsetHeight).toString() + "px"