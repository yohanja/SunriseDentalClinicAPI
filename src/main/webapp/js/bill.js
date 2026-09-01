let currentBillId = null;

document.getElementById("billForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const appointmentId = document.getElementById("appointmentId").value;
    const messageBox = document.getElementById("message");
    const receipt = document.getElementById("receipt");

    receipt.style.display = "none";
    document.getElementById("printBtn").style.display = "none";
    document.getElementById("markPaidBtn").style.display = "flex";
    messageBox.textContent = "";

    const formData = new URLSearchParams();
    formData.append("appointmentId", appointmentId);

    fetch("bill", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                messageBox.textContent = data.error;
                messageBox.className = "error";
                return;
            }

            currentBillId = data.billId;

            document.getElementById("rBillId").textContent = data.billId;
            document.getElementById("rAppointmentId").textContent = data.appointmentId;
            document.getElementById("rPatientName").textContent = data.patientName;
            document.getElementById("rAddress").textContent = data.address;
            document.getElementById("rContact").textContent = data.contactNumber;
            document.getElementById("rDentist").textContent = data.dentistName;
            document.getElementById("rTreatment").textContent = data.treatmentType;
            document.getElementById("rDate").textContent = data.appointmentDate;
            document.getElementById("rTime").textContent = data.appointmentTime;
            document.getElementById("rTreatmentCost").textContent = "Rs. " + data.treatmentCost.toFixed(2);
            document.getElementById("rConsultationFee").textContent = "Rs. " + data.consultationFee.toFixed(2);
            document.getElementById("rAmount").textContent = "Rs. " + data.amount.toFixed(2);
            document.getElementById("rStatus").textContent = data.paymentStatus;

            if (data.paymentStatus === "Paid") {
                document.getElementById("markPaidBtn").style.display = "none";
                document.getElementById("printBtn").style.display = "flex";
            }

            receipt.style.display = "block";
            messageBox.textContent = "";
        })
        .catch(error => {
            messageBox.textContent = "Something went wrong. Please try again.";
            messageBox.className = "error";
            console.error(error);
        });
});

document.getElementById("markPaidBtn").addEventListener("click", function() {
    if (!currentBillId) return;

    const formData = new URLSearchParams();
    formData.append("action", "markPaid");
    formData.append("billId", currentBillId);

    fetch("bill", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                document.getElementById("rStatus").textContent = "Paid";
                document.getElementById("markPaidBtn").style.display = "none";
                document.getElementById("printBtn").style.display = "flex";
            } else {
                alert(data.error);
            }
        })
        .catch(error => console.error(error));
});

document.getElementById("printBtn").addEventListener("click", function() {
    window.print();
});