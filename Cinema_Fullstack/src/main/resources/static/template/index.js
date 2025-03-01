let date = new Date();
let time = date.toLocaleTimeString([], {timeStyle: "short"});
document.getElementById("time").innerText = time;

// ----- chạy thời gian thực -----
function localeTime() {
    let date = new Date();
    let time = date.toLocaleTimeString([], {timeStyle: "short"});
    document.getElementById("time").innerText = time;
}

setInterval(localeTime, 1000);