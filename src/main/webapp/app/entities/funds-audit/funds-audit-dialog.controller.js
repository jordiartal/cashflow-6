(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('FundsAuditDialogController', FundsAuditDialogController);

    FundsAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FundsAudit', 'Funds'];

    function FundsAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FundsAudit, Funds) {
        var vm = this;
        vm.fundsAudit = entity;
        vm.funds = Funds.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('cashflow6App:fundsAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.fundsAudit.id !== null) {
                FundsAudit.update(vm.fundsAudit, onSaveSuccess, onSaveError);
            } else {
                FundsAudit.save(vm.fundsAudit, onSaveSuccess, onSaveError);
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
