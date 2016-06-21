(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('DepositsDialogController', DepositsDialogController);

    DepositsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Deposits', 'Currency', 'User'];

    function DepositsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Deposits, Currency, User) {
        var vm = this;
        vm.deposits = entity;
        vm.currencies = Currency.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:depositsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.deposits.id !== null) {
                Deposits.update(vm.deposits, onSaveSuccess, onSaveError);
            } else {
                Deposits.save(vm.deposits, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.initialDate = false;
        vm.datePickerOpenStatus.expDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
