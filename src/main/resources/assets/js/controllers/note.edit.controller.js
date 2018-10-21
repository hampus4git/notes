(function () {
    'use strict';

    angular
        .module('app')
        .controller('NoteEditController', NoteEditController);

    NoteEditController.$inject = ['$location', '$routeParams', 'NoteService'];
    function NoteEditController($location, $routeParams, NoteService) {
        var vm = this;
        vm.redirectListNotes = redirectListNotes;
        vm.editNote = editNote;
        fill();


        function fill(){
            NoteService.Show('{"id":"' + $routeParams.id + '"}', function(response){
                if (response.data.status == 'success' && response.data.data != null) {
                    vm.title = response.data.data.title;
                    vm.content = response.data.data.content;
                } else {
                    alert("Note can not be shown for unknown reason!")
                }
            });
        };

        function editNote(){
            NoteService.Edit('{"id":"' + $routeParams.id + '", "title":"' + vm.title + '", "content":"' + vm.content + '"}', function(response){
                if (response.data.status == 'success') {
                    alert("Note edited");
                    $location.path('/')
                } else {
                    alert("Could not edit note for unknown reason!")
                }
            });
        };

        function redirectListNotes(){
            $location.path('/');
        };
    }

})();