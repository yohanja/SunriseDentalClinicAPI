document.querySelectorAll(".accordion-header").forEach(header => {
    header.addEventListener("click", function() {
        const item = this.closest(".accordion-item");
        const isOpen = item.classList.contains("open");

        document.querySelectorAll(".accordion-item").forEach(i => i.classList.remove("open"));

        if (!isOpen) {
            item.classList.add("open");
        }
    });
});

document.querySelectorAll(".quicklink-btn").forEach(btn => {
    btn.addEventListener("click", function() {
        const targetId = this.getAttribute("data-target");
        const target = document.getElementById(targetId);

        document.querySelectorAll(".accordion-item").forEach(i => i.classList.remove("open"));
        target.classList.add("open");

        target.scrollIntoView({ behavior: "smooth", block: "center" });

        target.classList.add("highlight");
        setTimeout(() => target.classList.remove("highlight"), 1200);
    });
});

document.getElementById("helpSearch").addEventListener("input", function() {
    const query = this.value.toLowerCase();

    document.querySelectorAll(".accordion-item").forEach(item => {
        const text = item.textContent.toLowerCase();
        item.style.display = text.includes(query) ? "block" : "none";
    });
});