// Section visibility management
function showSection(sectionName) {
    document.getElementById('dashboardSection').classList.add('hidden');
    document.getElementById('bookingsSection').classList.add('hidden');
    document.getElementById('turfsSection').classList.add('hidden');
    document.getElementById(sectionName + 'Section').classList.remove('hidden');
    document.getElementById('sectionTitle').innerText = sectionName.charAt(0).toUpperCase() + sectionName.slice(1);
}

function showTurfForm() {
    document.getElementById('turfFormModal').classList.remove('hidden');
}

function hideTurfForm() {
    document.getElementById('turfFormModal').classList.add('hidden');
    document.getElementById('turfForm').reset();
    document.getElementById('turfId').value = '';
}

async function addTimeSlotForm(turfId) {
    let turfD;
    try {
        const turf = await fetch(`/api/turfs/${turfId}`, { // Use backticks for template literals
            method: 'GET',
            credentials: 'include' // Important for session/cookie handling
        });

        if (!turf.ok) {
            // Handle HTTP errors properly
            const errorText = await turf.text(); // Get error message from server
            throw new Error(`HTTP error ${turf.status}: ${errorText}`);
        }

        const turfData = await turf.json(); // Parse JSON response
        turfD = turfData;
        console.log("Turf Data:", turfData);
    } catch (error) {
        console.error("Error fetching turf:", error);
        alert("An error occurred while fetching turf data."); // Inform the user
    }
    console.log('Add Time Slot for Turf ID:', turfId);

    document.getElementById('addTime').addEventListener('click', async function (event) {
        event.preventDefault();

        const bookingDate = document.getElementById('bookingDate').value;
        const startTime = document.getElementById('startTime').value;
        const endTime = document.getElementById('endTime').value;

        // Convert time strings to objects
        const startTimeParts = startTime.split(':');
        const endTimeParts = endTime.split(':');

        const requestBody = {
            turf: turfD,
            start_time: `${startTimeParts[0].padStart(2, '0')}:${startTimeParts[1].padStart(2, '0')}:00`, // Format as "HH:mm:ss"
            end_time: `${endTimeParts[0].padStart(2, '0')}:${endTimeParts[1].padStart(2, '0')}:00`,     // Format as "HH:mm:ss"
            date: bookingDate,
            isAvailable: true // Default availability
        };
        console.log("Request Body:", requestBody);
        try {
            // Make the POST request
            const response = await fetch('/api/slots', {
                method: 'POST',
                credentials: 'include', // Include credentials for cookies/authentication
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody)
            });
    
            // Handle response
            if (!response.ok) {
                const errorText = await response.text(); // Await the response text
                throw new Error(`HTTP error ${response.status}: ${errorText}`);
            }
    
            const responseData = await response.json(); // Await the JSON response
            console.log("Success:", responseData);
            alert("TimeSlot created successfully!");
    
            // Clear the form fields after successful submission
            document.getElementById('bookingDate').value = '';
            document.getElementById('startTime').value = '';
            document.getElementById('endTime').value = '';
            document.getElementById('price').value = '';
            form.classList.add('hidden'); // Hide the form
        } catch (error) {
            console.error("Error creating Slot:", error);
            alert("An error occurred while creating the Timeslot. Please try again.");
        }
        hideTimeslotForm();
    });
}


function hideTimeslotForm() {
    document.getElementById('timeslotForm').classList.add('hidden');
    document.getElementById('form').reset();
}

function editTurf(id) {
    const turf = turfs.find(t => t.id == id);
    if (turf) {
        document.getElementById('turfId').value = turf.id;
        document.getElementById('turfName').value = turf.name;
        document.getElementById('turfType').value = turf.type;
        document.getElementById('turfCapacity').value = turf.capacity;
        document.getElementById('turfPrice').value = turf.price;
        showTurfForm();
    }
}

function deleteTurf(id) {
    // Show confirmation dialog
    if (confirm('Are you sure you want to delete this turf? This action cannot be undone.')) {
        fetch(`/api/turfs/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete turf');
            }
            // Show success message
            alert('Turf deleted successfully');
            // Re-render the turfs list
            renderTurfs();
        })
        .catch(error => {
            console.error('Error deleting turf:', error);
            alert('Failed to delete turf. Please try again.');
        });
    }
}

async function getUserDetails() {
    try {
        // Fetch the logged-in user's basic information
        const userResponse = await fetch('/api/users/me', { method: 'GET', credentials: 'include' });
        if (!userResponse.ok) {
            throw new Error(`Failed to fetch user info: ${await userResponse.text()}`);
        }
        const userData = await userResponse.json();
        const userId = userData.user_id;

        console.log(`User ID: ${userId}`);

        // Fetch the full user details using the user ID
        const userDetailsResponse = await fetch(`/api/users/${userId}`, { method: 'GET', credentials: 'include' });
        if (!userDetailsResponse.ok) {
            throw new Error(`Failed to fetch user details: ${await userDetailsResponse.text()}`);
        }
        const userDetails = await userDetailsResponse.json();

        console.log('User Details:', userDetails);

        return userDetails; // Return the user details
    } catch (error) {
        console.error('Error fetching user details:', error);
        throw error; // Re-throw the error for further handling if needed
    }
}

document.getElementById('save').addEventListener('click', async function (event) {
    event.preventDefault(); // Prevent form submission

    // Collect and validate inputs
    const name = document.getElementById('name').value.trim();
    const location = document.getElementById('location').value.trim();
    const pricePerHour = document.getElementById('pricePerHour').value.trim();
    const width = document.getElementById('ground_width').value.trim();
    const length = document.getElementById('ground_length').value.trim();
    const height = document.getElementById('ground_height').value.trim();
    const imageInput = document.getElementById('image');
    const imageFile = imageInput.files[0];

    if (!name || !location || !pricePerHour || !width || !length || !height || !imageFile) {
        alert('All fields are required.');
        return;
    }

    let imageUrl = '';
    if (imageFile) {
        // Step 1: Upload the image and get the URL
        const formData = new FormData();
        formData.append('image', imageFile);

        try {
            const response = await fetch('/api/upload', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            const result = await response.json();
            imageUrl = result.url; // URL of the uploaded image
            console.log(imageUrl);
        } catch (error) {
            console.error('Error uploading image:', error);
            alert('Failed to upload the image. Please try again.');
            return;
        }
    }

    try {
        const userD = await getUserDetails();
        // Create FormData to handle file upload
        const formData = new FormData();
        formData.append('name', name);
        formData.append('location', location);
        formData.append('pricePerHour', pricePerHour);
        formData.append('ground_width', width);
        formData.append('ground_length', length);
        formData.append('ground_height', height);
        formData.append('image', imageUrl);
        formData.append('manager', JSON.stringify(userD));

        console.log("This is form Data: ",formData);

        // Create turf
        const turfResponse = await fetch('/api/turfs', {
            method: 'POST',
            credentials: 'include',
            body: formData
        });

        if (!turfResponse.ok) throw new Error(`Turf creation failed: ${await turfResponse.text()}`);

        const result = await turfResponse.json();
        console.log("Turf created successfully:", result);
        alert("Turf successfully created!");
        hideTurfForm();
    } catch (error) {
        console.error('Error:', error);
        alert("There was an issue with your Turf Creation. Please try again.");
    }
});

// Turfs management and Bookings
document.addEventListener('DOMContentLoaded', function () {
    // Render turfs in the table
    renderTurfs();
    //Bookings in Table
    fetchAndRenderAllTurfs();
});

async function renderTurfs() {
    const turfsTableBody = document.getElementById('turfsTableBody');
    const userData = await getUserDetails();
    const profile = document.getElementById('prfname');
    profile.textContent = userData.username;

    const turfs = userData.turfs;
    console.log(turfs);
    // Update the turf count
    const turfCount = document.getElementById('Turfcnt');
    turfCount.textContent = turfs.length; // Display number of turfs
    turfsTableBody.innerHTML = ''; // Clear existing rows

    turfs.forEach(turf => {
        const timeslotButtonId = `timeslotbtn${turf.turf_id}`;
        const row = `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${turf.name}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${turf.location}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${turf.pricePerHour}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${turf.ground_width} x ${turf.ground_length} x ${turf.ground_height}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${new Date(turf.createdAt).toLocaleString()}</td>
                <td style="display: flex; align-items: center;" class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <!--<button style="margin: 9px; background-color: #0000ff7a; height: 30px; width: 60px; border-radius: 8px; color: white" onclick="editTurf(${turf.turf_id})" class="text-white-800 hover:text-grey-900 mr-2">Edit</button>-->
                    <button style="margin: 9px; background-color:rgb(210, 33, 33); height: 30px; width: 60px; border-radius: 8px; color: white" onclick="deleteTurf(${turf.turf_id})" class="text-white-800 hover:text-grey-900">Delete</button>
                    <button class="timeslot-btn" data-turf-id="${turf.turf_id}" style="margin: 9px; background-color:rgb(12, 242, 238); height: 30px; width: 70px; border-radius: 8px; color: black">Timeslots</button>
                </td>
            </tr>
        `;
        turfsTableBody.innerHTML += row;
        attachTimeslotListeners();
    });
}

function attachTimeslotListeners() {
    // Select all buttons with the class 'timeslot-btn'
    const timeslotButtons = document.querySelectorAll('.timeslot-btn');
    
    // Add click event listener to each button
    timeslotButtons.forEach(button => {
        button.addEventListener('click', () => {
            const turfId = button.dataset.turfId; // Get turf_id from the data attribute
            OpenTimeSlot(turfId); // Call the addTimeSlotForm function with turfId
        });
    });
}

function OpenTimeSlot(turfId) {
    // Display the form
    const form = document.getElementById('timeslotForm');
    form.classList.remove('hidden'); // Show the form
    addTimeSlotForm(turfId);
}

// Bookings management

async function fetchBookingsForAllTurfs(turfs) {
    const bookingsTableBody = document.getElementById('bookingsTableBody');
    bookingsTableBody.innerHTML = ''; // Clear the table body

    for (const turf of turfs) {
        try {
            const response = await fetch(`/api/bookings/turf/${turf.turf_id}`,{
                method: 'GET',
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch bookings for Turf ID ${turf.turf_id}: ${response.statusText}`);
            }

            const bookings = await response.json(); // Assuming the API returns a JSON response


            // Render each turf's bookings
            renderTurfWithBookings(turf, bookings);
        } catch (error) {
            console.error(`Error fetching bookings for Turf ID ${turf.turf_id}:`, error);
        }
    }
}
let cnt = 0;
function renderTurfWithBookings(turf, bookings) {
    //Count of Bookings:
    if(bookings){
        cnt += bookings.length;
        console.log(cnt);
        const bookcnt = document.getElementById('upcbook');
            if (bookcnt) {
                bookcnt.textContent = cnt; // Update the text with the bookings count
            }
    }
    const bookingsTableBody = document.getElementById('bookingsTableBody');
    bookings.forEach(booking => {
        const row = `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${booking.user.username}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.turf.name}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.booking_date}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.slot.start_time} - ${booking.slot.end_time}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.status}</td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium" style="width: 6%;">
                    <button 
                        style="margin-left: 20px; padding: 10px; background-color: white; transition: transform 0.6s ease-in-out;" 
                        class="text-green-600 hover:text-green-900 bg-transparent border cursor-pointer p-0 ml-2 grow-on-hover" 
                        title="Approve" 
                        onclick="updateBookingStatus(${booking.booking_id}, 'approved')">
                        &#10003; <!-- Checkmark symbol -->
                    </button>
                    <button 
                        style="margin-left: 20px; padding: 10px; background-color: white; transition: transform 0.6s ease-in-out;" 
                        class="text-red-600 hover:text-red-900 bg-transparent border cursor-pointer p-0 ml-2 grow-on-hover" 
                        title="Reject" 
                        onclick="updateBookingStatus(${booking.booking_id}, 'rejected')">
                        &#10005; <!-- Cross symbol -->
                    </button>
                </td>
            </tr>
        `;
        bookingsTableBody.innerHTML += row;
    });
    const dasTable = document.getElementById('dashTable');
    bookings.forEach(booking => {
        const row = `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${booking.booking_id}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${booking.user.username}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.turf.name}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.booking_date} & ${booking.slot.start_time} - ${booking.slot.end_time}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.status}</td>
            </tr>
        `;
        dasTable.innerHTML += row;
    })
}

//Updating the Booking Status
function updateBookingStatus(bookingId, status) {
    console.log(bookingId,status);
    fetch(`/api/bookings/${bookingId}/${status}`, {
        method: 'PUT', // Use PUT since you're updating the status
        credentials: 'include'
    })
    .then(response => response.json())
    .then(updatedBooking => {
        showToast('Booking status updated successfully!');
        // Handle UI updates here
    })
    .catch(error => {
        console.error('Error updating booking status:', error);
    });
    fetchAndRenderAllTurfs();
}

async function fetchAndRenderAllTurfs() {
    try {
        const response = await getUserDetails(); 
        console.log("Response:", response);

        // Check if turfs exist in the response
        if (response && response.turfs) {
            const turfs = response.turfs;
            console.log("Turfs:", turfs);
            await fetchBookingsForAllTurfs(turfs); // Process turfs
        } else {
            console.log("Error: Turfs data is missing in the response.");
        }
    } catch (error) {
        console.error('Error fetching turfs:', error);
    }
}

function showToast(message) {
    console.log('Showing toast:', message);
    const toastElement = document.getElementById('errorToast');
    const toastBody = toastElement.querySelector('.toast-body'); // Target the text container
    toastBody.innerText = message; // Set dynamic message
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}