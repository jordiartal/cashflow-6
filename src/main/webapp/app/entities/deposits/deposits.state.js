(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('deposits', {
            parent: 'entity',
            url: '/deposits?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.deposits.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deposits/deposits.html',
                    controller: 'DepositsController',
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
                    $translatePartialLoader.addPart('deposits');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('deposits-detail', {
            parent: 'entity',
            url: '/deposits/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.deposits.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deposits/deposits-detail.html',
                    controller: 'DepositsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deposits');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Deposits', function($stateParams, Deposits) {
                    return Deposits.get({id : $stateParams.id});
                }]
            }
        })
        .state('deposits.new', {
            parent: 'deposits',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposits/deposits-dialog.html',
                    controller: 'DepositsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                companyName: null,
                                initialValue: null,
                                finalValue: null,
                                initialDate: null,
                                expDate: null,
                                interest: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('deposits', null, { reload: true });
                }, function() {
                    $state.go('deposits');
                });
            }]
        })
        .state('deposits.edit', {
            parent: 'deposits',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposits/deposits-dialog.html',
                    controller: 'DepositsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deposits', function(Deposits) {
                            return Deposits.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('deposits', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deposits.delete', {
            parent: 'deposits',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deposits/deposits-delete-dialog.html',
                    controller: 'DepositsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Deposits', function(Deposits) {
                            return Deposits.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('deposits', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
