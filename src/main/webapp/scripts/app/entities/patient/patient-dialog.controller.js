'use strict';

angular.module('cmsApp').controller('PatientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Patient',
        function($scope, $stateParams, $modalInstance, entity, Patient) {

        $scope.patient = entity;
        $scope.load = function(id) {
            Patient.get({id : id}, function(result) {
                $scope.patient = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:patientUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.patient.id != null) {
                Patient.update($scope.patient, onSaveFinished);
            } else {
                Patient.save($scope.patient, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
