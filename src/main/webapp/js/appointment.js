let activeTab = "new"; // tracks which tab is currently selected

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

// Tab switching
document.getElementById("newPatientTabBtn").addEventListener("click", function() {
    activeTab = "new";
    document.getElementById("newPatientTabBtn").classList.add("active");
    document.getElementById("existingPatientTabBtn").classList.remove("active");
    document.getElementById("newPatientSection").style.display = "block";
    document.getElementById("existingPatientSection").style.display = "none";
});

document.getElementById("existingPatientTabBtn").addEventListener("click", function() {
    activeTab = "existing";
    document.getElementById("existingPatientTabBtn").classList.add("active");
    document.getElementById("newPatientTabBtn").classList.remove("active");
    document.getElementById("existingPatientSection").style.display = "block";
    document.getElementById("newPatientSection").style.display = "none";
});

// Find Patient button
document.getElementById("findPatientBtn").addEventListener("click", function() {
    const contact = document.getElementById("searchContact").value;
    const findMessage = document.getElementById("findPatientMessage");
    const detailsBox = document.getElementById("foundPatientDetails");

    if (!contact) {
        findMessage.textContent = "Please enter a contact number.";
        findMessage.className = "error";
        return;
    }

    fetch("lookup?type=patientByContact&contact=" + encodeURIComponent(contact))
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                findMessage.textContent = data.error;
                findMessage.className = "error";
                detailsBox.style.display = "none";
            } else {
                findMessage.textContent = "Patient found.";
                findMessage.className = "success";

                document.getElementById("existingPatientName").value = data.name;
                document.getElementById("existingContactNumber").value = data.contact;
                document.getElementById("existingEmail").value = data.email;
                document.getElementById("existingAddress").value = data.address;

                detailsBox.style.display = "block";
            }
        })
        .catch(error => {
            findMessage.textContent = "Something went wrong. Please try again.";
            findMessage.className = "error";
            console.error(error);
        });
});

// Shared booking function - used by both New Patient and Existing Patient tabs
function submitAppointment(patientName, address, contactNumber, email, messageBox) {
    const formData = new URLSearchParams();
    formData.append("patientName", patientName);
    formData.append("address", address);
    formData.append("contactNumber", contactNumber);
    formData.append("email", email);
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
                document.getElementById("foundPatientDetails").style.display = "none";
                document.getElementById("findPatientMessage").textContent = "";
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
}

document.getElementById("appointmentForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("message");
    let patientName, address, contactNumber, email;

    if (activeTab === "new") {
        patientName = document.getElementById("patientName").value;
        address = document.getElementById("address").value;
        contactNumber = document.getElementById("contactNumber").value;
        email = document.getElementById("email").value;

        if (!patientName || !address || !contactNumber || !email) {
            messageBox.textContent = "Please fill in all patient details.";
            messageBox.className = "error";
            return;
        }

        // Check if this contact number already belongs to someone else
        fetch("lookup?type=patientByContact&contact=" + encodeURIComponent(contactNumber))
            .then(response => response.json())
            .then(data => {
                if (!data.error && data.name.trim().toLowerCase() !== patientName.trim().toLowerCase()) {
                    messageBox.textContent = "This contact number is already registered under \"" + data.name + "\". Please use the Existing Patient tab, or check the contact number.";
                    messageBox.className = "error";
                    return;
                }
                submitAppointment(patientName, address, contactNumber, email, messageBox);
            })
            .catch(error => {
                messageBox.textContent = "Something went wrong. Please try again.";
                messageBox.className = "error";
                console.error(error);
            });

        return; // stop here - submitAppointment() runs after the check above finishes
    } else {
        patientName = document.getElementById("existingPatientName").value;
        address = document.getElementById("existingAddress").value;
        contactNumber = document.getElementById("existingContactNumber").value;
        email = document.getElementById("existingEmail").value;

        if (!contactNumber) {
            messageBox.textContent = "Please find and select an existing patient first.";
            messageBox.className = "error";
            return;
        }

        submitAppointment(patientName, address, contactNumber, email, messageBox);
    }
});