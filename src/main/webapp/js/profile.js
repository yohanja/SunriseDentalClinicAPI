window.addEventListener("DOMContentLoaded", function() {
    fetch("profile")
        .then(response => response.json())
        .then(data => {
            if (data.error) return;

            document.getElementById("profileAvatar").textContent = data.username.charAt(0).toUpperCase();
            document.getElementById("profileUsername").textContent = data.username;
            document.getElementById("profileRole").textContent = data.role;
        })
        .catch(error => console.error(error));
});

document.getElementById("passwordForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("message");
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (newPassword !== confirmPassword) {
        messageBox.textContent = "New password and confirmation do not match.";
        messageBox.className = "error";
        return;
    }

    const formData = new URLSearchParams();
    formData.append("action", "changePassword");
    formData.append("currentPassword", document.getElementById("currentPassword").value);
    formData.append("newPassword", newPassword);

    fetch("profile", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                messageBox.textContent = data.message;
                messageBox.className = "success";
                document.getElementById("passwordForm").reset();
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