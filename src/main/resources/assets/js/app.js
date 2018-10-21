(function () {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngCookies'])
        .config(config)
        .run(run);

    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/login', {
                controller: 'LoginController',
                templateUrl: '/views/user.login.view.html',
                controllerAs: 'vm'
            })

            .when('/register', {
                controller: 'UserRegisterController',
                templateUrl: '/views/user.register.view.html',
                controllerAs: 'vm'
            })

            .when('/create', {
                controller: 'NoteCreateController',
                templateUrl: '/views/note.create.view.html',
                controllerAs: 'vm'
            })

            .when('/edit/:id', {
                controller: 'NoteEditController',
                templateUrl: '/views/note.edit.view.html',
                controllerAs: 'vm'
            })

            .when('/', {
                controller: 'NoteListController',
                templateUrl: '/views/note.list.view.html',
                controllerAs: 'vm'
            })

            .when('/show/:id', {
                controller: 'NoteShowController',
                templateUrl: '/views/note.show.view.html',
                controllerAs: 'vm'
            })

            .otherwise({ redirectTo: '/login' });
    }

    run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
    function run($rootScope, $location, $cookies, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookies.getObject('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }

})();