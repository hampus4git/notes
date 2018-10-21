(function () {
    'use strict';

    angular
        .module('app')
        .factory('NoteService', NoteService);

    NoteService.$inject = ['$http'];
    function NoteService($http) {
        var service = {};

        service.Create = Create;
        service.Delete = Delete;
        service.Edit = Edit;
        service.List = List;
        service.Show = Show;

        return service;

        function List(callback) {
            $http.get('/api/note/list')
                .then(function successCallback(response) {
                    callback(response);
                }, function errorCallback(response){
                    callback(response);
                });

        }

        function Show(note, callback) {
            Post('api/note/show', note, callback)
        }

        function Edit(note, callback) {
            Post('api/note/edit', note, callback)
        }

        function Create(note, callback) {
             Post('api/note/create', note, callback)
        }

        function Delete(note, callback) {
            Post('api/note/delete', note, callback)
        }



        function Post(url, value, callback) {
            $http.post(url, value)
                .then(function successCallback(response){
                    callback(response)
                }, function errorCallback(response){
                    callback(response)
                });

        }
    }
}());