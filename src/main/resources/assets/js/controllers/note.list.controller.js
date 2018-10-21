(function () {
    'use strict';

    angular
        .module('app')
        .controller('NoteListController', NoteListController);

    NoteListController.$inject = ['$location', '$route', 'NoteService', 'UserService'];
    function NoteListController($location, $route, NoteService, UserService) {
        var vm = this;
        vm.redirectCreateNote = redirectCreateNote;
        vm.redirectShowNote = redirectShowNote;
        vm.redirectEditNote = redirectEditNote;
        vm.deleteNote = deleteNote;
        vm.logout = logout;
        list();

        function list() {
            vm.dataLoading = true;
            NoteService.List(function (response) {
                if (response.status == 200) {
                   vm.noteList = response.data.data;
                } else {
                    vm.noteList = "failure";
                }
            });
        };

        function redirectCreateNote(){
            $location.path('/create');
        };

        function redirectShowNote(note){
            $location.path('/show/' + note.id);
        };

        function redirectEditNote(note){
            $location.path('/edit/' + note.id);
        };

        function logout(){
            UserService.ClearCredentials();
            $location.path('login');
        }

        function deleteNote(note){
            NoteService.Delete(note, function(response){
                if (response.data.status == 'success') {
                    alert("The note has been removed!")
                    $route.reload();
                } else {
                    alert("Could not remove note for unknown reason!")
                }
            });
        }
    }

})();