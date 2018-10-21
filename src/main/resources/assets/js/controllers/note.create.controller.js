(function () {
    'use strict';

    angular
        .module('app')
        .controller('NoteCreateController', NoteCreateController);

    NoteCreateController.$inject = ['$location', 'NoteService'];
    function NoteCreateController($location, NoteService) {
        var vm = this;
        vm.createNote = createNote;
        vm.redirectListNotes = redirectListNotes;


        function createNote(){
            NoteService.Create('{"title": "' + vm.title +'", "content": "' + vm.content + '"}', function(response){
               if(response.data.status == 'success'){
                    alert('Note created!');
                    $location.path('/');
               }else{
                    alert('Could not create note for unknown reason!');
               }
            });
        }

         function redirectListNotes(){
                $location.path('/');
            };
    }

})();