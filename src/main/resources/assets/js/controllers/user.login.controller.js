(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'UserService'];
    function LoginController($location, UserService) {
        var vm = this;

        vm.login = login;
        vm.register = register;

        (function initController() {
            // reset login status
            UserService.ClearCredentials();
        })();

        function login() {
            vm.dataLoading = true;
            UserService.Login(vm.username, vm.password, function (response) {
                if (response.data.status == 'success') {
                    UserService.SetCredentials(vm.username, vm.password);
                    $location.path('/');
                } else {
                    UserService.ClearCredentials()
                    vm.dataLoading = false;
                    alert("Login incorrect!");
                }
            });
        };

        function register(){
            $location.path('/register');
        };
    }

})();