'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('examens', {
                parent: 'entity',
                url: '/examenss',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.examens.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/examens/examenss.html',
                        controller: 'ExamensController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('examens');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('examens.detail', {
                parent: 'entity',
                url: '/examens/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.examens.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/examens/examens-detail.html',
                        controller: 'ExamensDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('examens');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Examens', function($stateParams, Examens) {
                        return Examens.get({id : $stateParams.id});
                    }]
                }
            })
            .state('examens.new', {
                parent: 'examens',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/examens/examens-dialog.html',
                        controller: 'ExamensDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {type: null, libelle: null, date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('examens', null, { reload: true });
                    }, function() {
                        $state.go('examens');
                    })
                }]
            })
            .state('examens.edit', {
                parent: 'examens',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/examens/examens-dialog.html',
                        controller: 'ExamensDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Examens', function(Examens) {
                                return Examens.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('examens', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
