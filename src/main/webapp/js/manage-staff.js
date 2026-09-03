function loadStaffList() {
    fetch("profile?action=allUsers")
        .then(response => {
            if (response.status === 403) {
                alert("Access denied. Admins only.");
                window.location.replace("dashboard.html");
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (!data || data.error) return;

            const tableBody = document.getElementById("staffTableBody");
            const dropdown = document.getElementById("resetUsernameSelect");

            tableBody.innerHTML = "";
            dropdown.innerHTML = '<option value="" disabled selected>-- Select Staff --</option>';

            data.users.forEach(user => {
                // Fill the table row
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.role}</td>
                `;
                tableBody.appendChild(row);

                // Fill the dropdown option
                const option = document.createElement("option");
                option.value = user.username;
                option.textContent = user.username + " (" + user.role + ")";
                dropdown.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

// Register new staff
document.getElementById("registerStaffForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("registerMessage");

    const formData = new URLSearchParams();
    formData.append("action", "registerStaff");
    formData.append("username", document.getElementById("newUsername").value);
    formData.append("password", document.getElementById("newStaffPassword").value);
    formData.append("role", document.getElementById("newStaffRole").value);

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
                document.getElementById("registerStaffForm").reset();
                loadStaffList();
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

// Reset staff password
document.getElementById("resetPasswordForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("resetMessage");
    const targetUsername = document.getElementById("resetUsernameSelect").value;
    const newPassword = document.getElementById("resetNewPassword").value;

    const formData = new URLSearchParams();
    formData.append("action", "adminResetPassword");
    formData.append("targetUsername", targetUsername);
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
                document.getElementById("resetPasswordForm").reset();
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

window.addEventListener("DOMContentLoaded", loadStaffList);