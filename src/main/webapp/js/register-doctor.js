window.addEventListener("DOMContentLoaded", function() {
    const role = sessionStorage.getItem("role");
    if (role !== "Admin") {
        alert("Access denied. This page is only available to Admin users.");
        window.location.replace("dashboard.html");
    }
});

document.getElementById("doctorForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("message");

    const formData = new URLSearchParams();
    formData.append("action", "addDentist");
    formData.append("name", document.getElementById("doctorName").value);
    formData.append("specialization", document.getElementById("specialization").value);

    fetch("lookup", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                messageBox.textContent = data.message;
                messageBox.className = "success";
                document.getElementById("doctorForm").reset();
            } else {
                messageBox.textContent = data.error;
                messageBox.className = "error";
            }
        })
        .catch(error => {
            messageBox.textContent = "Something went wrong. Please try again.";
            messageBox.className = "error";
            console.error(error);
        });
});