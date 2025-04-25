document.addEventListener('DOMContentLoaded', () => {
    const protectedRoutes = ['/dashboard', '/profile', /* ... other protected routes */];
    const currentPath = window.location.pathname;

    function redirectToLogin() {
        window.location.href = '/login';
    }

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    function handleProtectedRoute(){
        const token = getCookie("jwtToken");
        if(token){
            fetch("http://localhost:8080/api/users/validateToken",{
                method:"GET",
                headers:{
                    "Authorization": `Bearer ${token}`
                }
            }).then(res=>res.json()).then(data=>{
                if(!data.isValid){
                    document.cookie = "jwtToken=; max-age=3600; path=/;";
                    redirectToLogin();
                }
            }).catch(err=>{
                document.cookie = "jwtToken=; max-age=3600; path=/;";
                redirectToLogin();
            })
        }
        else{
            redirectToLogin();
        }
    }

    if (protectedRoutes.includes(currentPath)) {
        handleProtectedRoute();
    }
});