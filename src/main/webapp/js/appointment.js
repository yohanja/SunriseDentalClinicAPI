window.addEventListener("DOMContentLoaded", function() {
    loadDentists();
});

function loadDentists() {
    fetch("lookup?type=dentists")
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById("dentistId");
            select.innerHTML = '<option value="">-- Select Dentist --</option>';
            data.forEach(dentist => {
                const option = document.createElement("option");
                option.value = dentist.id;
                option.textContent = dentist.name;
                select.appendChild(option);
            });
        })
        .catch(error => console.error("Failed to load dentists:", error));
}

document.getElementById("appointmentForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("message");

    const formData = new URLSearchParams();
    formData.append("patientName", document.getElementById("patientName").value);
    formData.append("address", document.getElementById("address").value);
    formData.append("contactNumber", document.getElementById("contactNumber").value);
    formData.append("treatmentType", document.getElementById("treatmentType").value);
    formData.append("dentistId", document.getElementById("dentistId").value);
    formData.append("appointmentDate", document.getElementById("appointmentDate").value);
    formData.append("appointmentTime", document.getElementById("appointmentTime").value);

    fetch("appointment", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                messageBox.textContent = data.message;
                messageBox.className = "success";
                document.getElementById("appointmentForm").reset();
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