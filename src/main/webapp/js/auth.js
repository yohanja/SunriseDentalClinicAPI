document.getElementById("logoutBtn").addEventListener("click", function() {
    const confirmLogout = confirm("Are you sure you want to logout?");
    if (confirmLogout) {
        sessionStorage.clear();
        window.location.replace("login.html");
    }
});

window.addEventListener("pageshow", function(event) {
    if (event.persisted || (window.performance && window.performance.getEntriesByType("navigation")[0].type === "back_forward")) {
        if (!sessionStorage.getItem("username")) {
            window.location.replace("login.html");
        }
    }
});