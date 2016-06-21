'use strict';

describe('Controller Tests', function() {

    describe('SavingsAudit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSavingsAudit, MockSavings;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSavingsAudit = jasmine.createSpy('MockSavingsAudit');
            MockSavings = jasmine.createSpy('MockSavings');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SavingsAudit': MockSavingsAudit,
                'Savings': MockSavings
            };
            createController = function() {
                $injector.get('$controller')("SavingsAuditDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:savingsAuditUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
