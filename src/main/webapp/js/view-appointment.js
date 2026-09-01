let currentMode = "id";

document.getElementById("tabById").addEventListener("click", function() {
    setMode("id");
});

document.getElementById("tabByContact").addEventListener("click", function() {
    setMode("contact");
});

function setMode(mode) {
    currentMode = mode;

    document.getElementById("tabById").classList.toggle("active", mode === "id");
    document.getElementById("tabByContact").classList.toggle("active", mode === "contact");

    document.getElementById("idField").style.display = mode === "id" ? "flex" : "none";
    document.getElementById("contactField").style.display = mode === "contact" ? "flex" : "none";

    document.getElementById("resultCard").style.display = "none";
    document.getElementById("resultList").style.display = "none";
    document.getElementById("message").textContent = "";
}

document.getElementById("searchForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const messageBox = document.getElementById("message");
    const resultCard = document.getElementById("resultCard");
    const resultList = document.getElementById("resultList");

    resultCard.style.display = "none";
    resultList.style.display = "none";
    messageBox.textContent = "";

    if (currentMode === "id") {
        const appointmentId = document.getElementById("appointmentId").value;

        fetch("appointment?id=" + appointmentId)
            .then(response => response.json())
            .then(data => {
                const raw = data.result;

                if (raw.startsWith("No appointment found") || raw.startsWith("Error")) {
                    messageBox.textContent = raw;
                    messageBox.className = "error";
                    return;
                }

                const parts = {};
                raw.split(", ").forEach(pair => {
                    const [key, value] = pair.split(": ");
                    parts[key.trim()] = value ? value.trim() : "";
                });

                document.getElementById("resId").textContent = parts["Appointment ID"] || "";
                document.getElementById("resPatient").textContent = parts["Patient"] || "";
                document.getElementById("resDentist").textContent = parts["Dentist"] || "";
                document.getElementById("resTreatment").textContent = parts["Treatment"] || "";
                document.getElementById("resDate").textContent = parts["Date"] || "";
                document.getElementById("resTime").textContent = parts["Time"] || "";

                resultCard.style.display = "block";
            })
            .catch(error => {
                messageBox.textContent = "Something went wrong. Please try again.";
                messageBox.className = "error";
                console.error(error);
            });

    } else {
        const contactNumber = document.getElementById("contactNumber").value;

        fetch("appointment?contact=" + contactNumber)
            .then(response => response.json())
            .then(data => {
                const results = data.results;

                if (!results || results.length === 0) {
                    messageBox.textContent = "No appointments found for this contact number.";
                    messageBox.className = "error";
                    return;
                }

                const tableBody = document.getElementById("listTableBody");
                tableBody.innerHTML = "";

                results.forEach(appt => {
                    const row = document.createElement("tr");
                    row.innerHTML =
                        "<td>" + appt.id + "</td>" +
                        "<td>" + appt.patient + "</td>" +
                        "<td>" + appt.dentist + "</td>" +
                        "<td>" + appt.treatment + "</td>" +
                        "<td>" + appt.date + "</td>" +
                        "<td>" + appt.time + "</td>";
                    tableBody.appendChild(row);
                });

                resultList.style.display = "block";
            })
            .catch(error => {
                messageBox.textContent = "Something went wrong. Please try again.";
                messageBox.className = "error";
                console.error(error);
            });
    }
});