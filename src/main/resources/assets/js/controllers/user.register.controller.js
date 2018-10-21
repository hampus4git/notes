(function () {
    'use strict';

    angular
        .module('app')
        .controller('UserRegisterController', UserRegisterController);

    UserRegisterController.$inject = ['$location', 'UserService'];
    function UserRegisterController($location, UserService) {
        var vm = this;

        vm.register = register;
        vm.redirectLogin = redirectLogin;

        function register() {
            vm.dataLoading = true;
            UserService.Register(vm.username, vm.password, function (response) {
                if (response.data.status == 'success') {
                    alert("User created");
                    $location.path('/login');
                } else if(response.data.status == 'username already present'){
                    alert("User with that name already exists!");
                }else{
                    alert("Both username and password field need to be filled in!")
                }
            });
        };

        function redirectLogin(){
            $location.path('/login');
        };
    }

})();