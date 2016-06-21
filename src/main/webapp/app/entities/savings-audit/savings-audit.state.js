(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('savings-audit', {
            parent: 'entity',
            url: '/savings-audit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.savingsAudit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/savings-audit/savings-audits.html',
                    controller: 'SavingsAuditController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('savingsAudit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('savings-audit-detail', {
            parent: 'entity',
            url: '/savings-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.savingsAudit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/savings-audit/savings-audit-detail.html',
                    controller: 'SavingsAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('savingsAudit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SavingsAudit', function($stateParams, SavingsAudit) {
                    return SavingsAudit.get({id : $stateParams.id});
                }]
            }
        })
        .state('savings-audit.new', {
            parent: 'savings-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/savings-audit/savings-audit-dialog.html',
                    controller: 'SavingsAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                opDate: null,
                                antValue: null,
                                newValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('savings-audit', null, { reload: true });
                }, function() {
                    $state.go('savings-audit');
                });
            }]
        })
        .state('savings-audit.edit', {
            parent: 'savings-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/savings-audit/savings-audit-dialog.html',
                    controller: 'SavingsAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SavingsAudit', function(SavingsAudit) {
                            return SavingsAudit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('savings-audit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('savings-audit.delete', {
            parent: 'savings-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/savings-audit/savings-audit-delete-dialog.html',
                    controller: 'SavingsAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SavingsAudit', function(SavingsAudit) {
                            return SavingsAudit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('savings-audit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
