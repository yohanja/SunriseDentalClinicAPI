document.getElementById("patientSearchForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const query = document.getElementById("searchQuery").value;
    const messageBox = document.getElementById("message");
    const resultsBox = document.getElementById("searchResults");
    const profileBox = document.getElementById("patientProfile");

    resultsBox.style.display = "none";
    profileBox.style.display = "none";
    messageBox.textContent = "";

    fetch("patient?search=" + encodeURIComponent(query))
        .then(response => response.json())
        .then(data => {
            const results = data.results;

            if (!results || results.length === 0) {
                messageBox.textContent = "No patients found matching that search.";
                messageBox.className = "error";
                return;
            }

            resultsBox.innerHTML = "";
            results.forEach(patient => {
                const item = document.createElement("div");
                item.className = "patient-result-item";
                item.innerHTML =
                    "<div><div class='pname'>" + patient.name + "</div>" +
                    "<div class='pcontact'>" + patient.contact + "</div></div>" +
                    "<i class='fa-solid fa-chevron-right'></i>";

                item.addEventListener("click", function() {
                    loadPatientProfile(patient.id);
                });

                resultsBox.appendChild(item);
            });

            resultsBox.style.display = "block";
        })
        .catch(error => {
            messageBox.textContent = "Something went wrong. Please try again.";
            messageBox.className = "error";
            console.error(error);
        });
});

function getInitials(name) {
    const parts = name.trim().split(" ");
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
    return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
}

document.getElementById("patientSearchForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const query = document.getElementById("searchQuery").value;
    const messageBox = document.getElementById("message");
    const resultsBox = document.getElementById("searchResults");
    const profileBox = document.getElementById("patientProfile");

    resultsBox.style.display = "none";
    profileBox.style.display = "none";
    messageBox.textContent = "";

    fetch("patient?search=" + encodeURIComponent(query))
        .then(response => response.json())
        .then(data => {
            const results = data.results;

            if (!results || results.length === 0) {
                messageBox.textContent = "No patients found matching that search.";
                messageBox.className = "error";
                return;
            }

            resultsBox.innerHTML = "";
            results.forEach(patient => {
                const item = document.createElement("div");
                item.className = "patient-result-item";
                item.innerHTML =
                    "<div class='result-avatar'>" + getInitials(patient.name) + "</div>" +
                    "<div><div class='pname'>" + patient.name + "</div>" +
                    "<div class='pcontact'>" + patient.contact + "</div></div>" +
                    "<i class='fa-solid fa-chevron-right'></i>";

                item.addEventListener("click", function() {
                    loadPatientProfile(patient.id);
                });

                resultsBox.appendChild(item);
            });

            resultsBox.style.display = "block";
        })
        .catch(error => {
            messageBox.textContent = "Something went wrong. Please try again.";
            messageBox.className = "error";
            console.error(error);
        });
});

function loadPatientProfile(patientId) {
    const messageBox = document.getElementById("message");
    const profileBox = document.getElementById("patientProfile");

    fetch("patient?id=" + patientId)
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                messageBox.textContent = data.error;
                messageBox.className = "error";
                return;
            }

            document.getElementById("pAvatar").textContent = getInitials(data.name);
            document.getElementById("pNameHeader").textContent = data.name;
            document.getElementById("pContactHeader").textContent = data.contact;
            document.getElementById("visitBadge").textContent = data.history.length + " Visit" + (data.history.length !== 1 ? "s" : "");

            document.getElementById("pId").textContent = data.id;
            document.getElementById("pAddress").textContent = data.address;
            document.getElementById("pEmail").textContent = data.email || "Not provided";

            const historyBody = document.getElementById("historyTableBody");
            historyBody.innerHTML = "";

            if (data.history.length === 0) {
                historyBody.innerHTML = "<tr><td colspan='5'>No appointment history.</td></tr>";
            } else {
                data.history.forEach(appt => {
                    const row = document.createElement("tr");
                    row.innerHTML =
                        "<td>" + appt.id + "</td>" +
                        "<td>" + appt.dentist + "</td>" +
                        "<td>" + appt.treatment + "</td>" +
                        "<td>" + appt.date + "</td>" +
                        "<td>" + appt.time + "</td>";
                    historyBody.appendChild(row);
                });
            }

            profileBox.style.display = "block";
            profileBox.scrollIntoView({ behavior: "smooth", block: "start" });
        })
        .catch(error => console.error(error));
}