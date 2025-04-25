// document.getElementById('registrationForm').addEventListener('submit', async function(e) {
//     e.preventDefault();
    
//     const name = document.getElementById('name').value;
//     const email = document.getElementById('email').value;
//     const phone = document.getElementById('phone').value;
//     const password = document.getElementById('password').value;
//     const role = document.getElementById('role').value;
//     const errorElement = document.getElementById('error');

//     // Reset error message
//     errorElement.textContent = '';

//     // Basic validation
//     if (!name || !email || !phone || !password || !role) {
//         errorElement.textContent = 'All fields are required';
//         return;
//     }

//     // Email validation
//     const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//     if (!emailRegex.test(email)) {
//         errorElement.textContent = 'Invalid email address';
//         return;
//     }

//     // Phone validation (simple check for numbers only)
//     const phoneRegex = /^\d+$/;
//     if (!phoneRegex.test(phone)) {
//         errorElement.textContent = 'Phone number should contain only digits';
//         return;
//     }

//     // Password strength check (at least 8 characters)
//     if (password.length < 8) {
//         errorElement.textContent = 'Password should be at least 8 characters long';
//         return;
//     }

//     // If all validations pass, you would typically send this data to your server
//     console.log('Form submitted:', { name, email, phone, password, role });
//     console.log('Registration successful!)');
//     try {
//         const response = await fetch('/reg/signup', {
//             method: "POST",
//             headers: {
//                 "Content-Type": "application/json",
//             },
//             body: JSON.stringify({ name, email, phone, password, role}),
//         });

//         if (response.ok) {
//             const data = await response.json();
//             document.getElementById("responseMessage").innerText = "Registration successful!";
//         } else {
//             const error = await response.json();
//             document.getElementById("responseMessage").innerText = `Error: ${error.message}`;
//         }
//     } catch (err) {
//         // document.getElementById("responseMessage").innerText = `Error: ${err.message}`;
//         console.log(err)
//     }
//     // In a real application, you would send this data to your server and handle the response
// });