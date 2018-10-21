(function () {
    'use strict';

    angular
        .module('app')
        .controller('NoteShowController', NoteShowController);

    NoteShowController.$inject = ['$location', '$routeParams', 'NoteService'];
    function NoteShowController($location, $routeParams, NoteService) {
        var vm = this;
        vm.redirectListNotes = redirectListNotes;
        show();


        function show(){
            NoteService.Show('{"id":"' + $routeParams.id + '"}', function(response){
               if (response.data.status == 'success' && response.data.data != null) {
                   vm.title = response.data.data.title;
                   vm.content = response.data.data.content;
               } else {
                    alert("Note can not be shown for unknown reason!")
               }
            });
        }

        function redirectListNotes(){
            $location.path('/');
        };
    }

})();