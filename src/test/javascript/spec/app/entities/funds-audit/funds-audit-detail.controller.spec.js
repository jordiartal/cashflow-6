'use strict';

describe('Controller Tests', function() {

    describe('FundsAudit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFundsAudit, MockFunds;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFundsAudit = jasmine.createSpy('MockFundsAudit');
            MockFunds = jasmine.createSpy('MockFunds');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FundsAudit': MockFundsAudit,
                'Funds': MockFunds
            };
            createController = function() {
                $injector.get('$controller')("FundsAuditDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:fundsAuditUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
