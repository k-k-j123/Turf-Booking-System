const loginButton = document.getElementById("loginButton");
loginButton.addEventListener('click', (event) => {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (!email || !password) {
        console.log("Please fill in all fields.");
        return;
    }

    const data = { email, password };
    const jsonData = JSON.stringify(data);

    fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: jsonData
    })
    .then(response => {
        console.log("Full Response:", response);

        return response.text().then(errorMessage => {
            if (!response.ok) {
                console.error("Server Error:", errorMessage);
                if (response.status === 403 && errorMessage === "Admin account not approved") {
                    showToast("Profile Under Approval");  // Correct message for unapproved admin
                } else {
                    showToast("Invalid Email or Password"); // Default error
                }
                throw new Error(errorMessage); // Stop further execution
            }
            return JSON.parse(errorMessage); // Parse JSON for success case
        });
    })
    .then(data => {
        console.log("Response Data:", data);

        if (data.message === "Login successful") {
            console.log("Should redirect to dashboard");
            setTimeout(() => {
                window.location.href = "/dashboard";
            }, 1200);
        }
    })
    .catch(error => {
        console.error('Fetch Error:', error);
    });

    // Function to show a toast message
    function showToast(message) {
        console.log('Showing toast:', message);
        const toastElement = document.getElementById('errorToast');
        const toastBody = toastElement.querySelector('.toast-body'); // Target the text container
        toastBody.innerText = message; // Set dynamic message
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }    
});
