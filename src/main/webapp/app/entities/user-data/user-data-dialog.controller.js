(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('UserDataDialogController', UserDataDialogController);

    UserDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserData', 'User'];

    function UserDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserData, User) {
        var vm = this;
        vm.userData = entity;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:userDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.userData.id !== null) {
                UserData.update(vm.userData, onSaveSuccess, onSaveError);
            } else {
                UserData.save(vm.userData, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.birthday = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
