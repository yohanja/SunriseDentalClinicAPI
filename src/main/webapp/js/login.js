document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const messageBox = document.getElementById("message");

    const formData = new URLSearchParams();
    formData.append("username", username);
    formData.append("password", password);

    fetch("login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                messageBox.textContent = data.message + " (" + data.role + ")";
                messageBox.className = "success";

                sessionStorage.setItem("username", username);
                sessionStorage.setItem("role", data.role);

                //clear the input fields
                document.getElementById("username").value = "";
                document.getElementById("password").value = "";

                setTimeout(function() {
                    window.location.href = "dashboard.html";
                }, 1000);
            } else {
                messageBox.textContent = data.error;
                messageBox.className = "error";
            }
            //clear the input fields
            document.getElementById("username").value = "";
            document.getElementById("password").value = "";

        })
        .catch(error => {
            messageBox.textContent = "Something went wrong. Please try again.";
            messageBox.className = "error";
            console.error(error);
        });

});