(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsAuditDialogController', SavingsAuditDialogController);

    SavingsAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SavingsAudit', 'Savings'];

    function SavingsAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SavingsAudit, Savings) {
        var vm = this;
        vm.savingsAudit = entity;
        vm.savings = Savings.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:savingsAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.savingsAudit.id !== null) {
                SavingsAudit.update(vm.savingsAudit, onSaveSuccess, onSaveError);
            } else {
                SavingsAudit.save(vm.savingsAudit, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.opDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
