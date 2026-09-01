window.addEventListener("DOMContentLoaded", function() {

    fetch("dashboard-data")
        .then(response => response.json())
        .then(data => {
            document.getElementById("dateDisplay").innerHTML =
                '<i class="fa-regular fa-calendar"></i> ' + data.date;
            document.getElementById("timeDisplay").innerHTML =
                '<i class="fa-regular fa-clock"></i> ' + data.time;

            document.getElementById("appointmentCount").textContent = data.appointmentCount;
            document.getElementById("dentistCount").textContent = data.dentistCount;
            document.getElementById("patientCount").textContent = data.patientCount;

            const tableBody = document.getElementById("upcomingTableBody");
            tableBody.innerHTML = "";

            if (data.upcomingAppointments.length === 0) {
                tableBody.innerHTML = "<tr><td colspan='5'>No upcoming appointments.</td></tr>";
            } else {
                data.upcomingAppointments.forEach(appt => {
                    const row = document.createElement("tr");
                    row.innerHTML =
                        "<td>" + appt.patient + "</td>" +
                        "<td>" + appt.dentist + "</td>" +
                        "<td>" + appt.treatment + "</td>" +
                        "<td>" + appt.date + "</td>" +
                        "<td>" + appt.time + "</td>";
                    tableBody.appendChild(row);
                });
            }
        })
        .catch(error => {
            console.error("Failed to load dashboard data:", error);
        });

    const username = sessionStorage.getItem("username") || "Staff";
    const role = sessionStorage.getItem("role") || "";
    document.getElementById("userDisplay").textContent = username;
    document.getElementById("roleDisplay").textContent = role;

});

