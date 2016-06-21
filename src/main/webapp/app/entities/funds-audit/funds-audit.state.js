(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('funds-audit', {
            parent: 'entity',
            url: '/funds-audit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.fundsAudit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/funds-audit/funds-audits.html',
                    controller: 'FundsAuditController',
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
                    $translatePartialLoader.addPart('fundsAudit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('funds-audit-detail', {
            parent: 'entity',
            url: '/funds-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.fundsAudit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/funds-audit/funds-audit-detail.html',
                    controller: 'FundsAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fundsAudit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FundsAudit', function($stateParams, FundsAudit) {
                    return FundsAudit.get({id : $stateParams.id});
                }]
            }
        })
        .state('funds-audit.new', {
            parent: 'funds-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funds-audit/funds-audit-dialog.html',
                    controller: 'FundsAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                opDate: null,
                                antValue: null,
                                newValue: null,
                                antShares: null,
                                newShares: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('funds-audit', null, { reload: true });
                }, function() {
                    $state.go('funds-audit');
                });
            }]
        })
        .state('funds-audit.edit', {
            parent: 'funds-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funds-audit/funds-audit-dialog.html',
                    controller: 'FundsAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FundsAudit', function(FundsAudit) {
                            return FundsAudit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('funds-audit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('funds-audit.delete', {
            parent: 'funds-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/funds-audit/funds-audit-delete-dialog.html',
                    controller: 'FundsAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FundsAudit', function(FundsAudit) {
                            return FundsAudit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('funds-audit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
