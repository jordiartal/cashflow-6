(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsDialogController', SavingsDialogController);

    SavingsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Savings', 'Currency', 'SavingsAudit', 'User'];

    function SavingsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Savings, Currency, SavingsAudit, User) {
        var vm = this;
        vm.savings = entity;
        vm.currencies = Currency.query();
        vm.savingsaudits = SavingsAudit.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:savingsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.savings.id !== null) {
                Savings.update(vm.savings, onSaveSuccess, onSaveError);
            } else {
                Savings.save(vm.savings, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.initialDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
