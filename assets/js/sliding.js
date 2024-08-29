let reducedMotion = false
if (localStorage.getItem("reducedMotion") == "true") {
    reducedMotion = true
}

localStorage.reducedMotion = reducedMotion
redMotDict =
{
    true: "On",
    false: "Off",
}
let redMotButton = document.getElementById("redMotButton")
redMotButton.innerText = "Reduced Motion: " + redMotDict[reducedMotion]
function toggleMot() {
    reducedMotion = !reducedMotion
    localStorage.reducedMotion = reducedMotion
    redMotButton.innerText = "Reduced Motion: " + redMotDict[reducedMotion]
}

const observer = new IntersectionObserver((entries) => {

    entries.forEach((entry) => {
        entry.target.style.animation = null;
        if (entry.isIntersecting && !reducedMotion) {
            window.setTimeout(() => observer.unobserve(entry.target), 1000);
            switch (entry.target.dataset.slideanim) {
                case "slideRight":
                    entry.target.style.animation += "slideRight 1s cubic-bezier(.77,.27,.48,.9)"
                    break;

                case "slideOpacity":
                    entry.target.style.animation += "slideOpacity 1s cubic-bezier(.77,.27,.48,.9)"
                    break;
                case "slideLeft":
                    entry.target.style.animation += "slideLeft 1s cubic-bezier(.77,.27,.48,.9)"
                    break;

                default:

                    break;
            }
            /*
            if(entry.target.dataset.slideanim == "")
        {
            entry.target.style.animation = "typewriter 3s ease .5s"
        }*/
        }
    })
})

const slidables = document.querySelectorAll(".slideable")
slidables.forEach((el) => observer.observe(el))