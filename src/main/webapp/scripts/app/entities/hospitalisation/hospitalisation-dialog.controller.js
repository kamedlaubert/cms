'use strict';

angular.module('cmsApp').controller('HospitalisationDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Hospitalisation', 'Consultation', 'BPC', 'InstitutionSanitaire',
        function($scope, $stateParams, $modalInstance, $q, entity, Hospitalisation, Consultation, BPC, InstitutionSanitaire) {

        $scope.hospitalisation = entity;
        $scope.consultations = Consultation.query();
        $scope.bpcs = BPC.query({filter: 'hospitalisation-is-null'});
        $q.all([$scope.hospitalisation.$promise, $scope.bpcs.$promise]).then(function() {
            if (!$scope.hospitalisation.bPC.id) {
                return $q.reject();
            }
            return BPC.get({id : $scope.hospitalisation.bPC.id}).$promise;
        }).then(function(bPC) {
            $scope.bpcs.push(bPC);
        });
        $scope.institutionsanitaires = InstitutionSanitaire.query();
        $scope.load = function(id) {
            Hospitalisation.get({id : id}, function(result) {
                $scope.hospitalisation = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:hospitalisationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.hospitalisation.id != null) {
                Hospitalisation.update($scope.hospitalisation, onSaveFinished);
            } else {
                Hospitalisation.save($scope.hospitalisation, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
